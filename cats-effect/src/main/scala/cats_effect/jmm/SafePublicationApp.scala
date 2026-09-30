package cats_effect.jmm

import cats.effect.{Deferred, IO, IOApp, Ref}
import cats.syntax.all._

import java.util.concurrent.{ConcurrentHashMap, LinkedBlockingQueue}

/** Safe publication: ways to hand an object to another thread so that it sees the object fully constructed.
  *   1. final fields (in Scala, a constructor `val` compiles to a final field)
  *   2. a volatile field
  *   3. concurrent collections (put HB get)
  *   4. handing it over through a lock
  *
  * cats-effect: Ref, Deferred and Queue are built on these, so values passed between fibers are always safely
  * published.
  */
object SafePublicationApp extends IOApp.Simple {

  // 1. Final fields: the constructor writes are frozen when the constructor ends, and they're visible even if the
  //    reference itself leaks through a data race. Only the final fields get this guarantee; a `var` doesn't.
  final class Point(val x: Int, val y: Int) // final fields: always safe
  final class MutablePoint(var x: Int, var y: Int) // non-final: can be seen as (0, 0) through a racy reference

  // Racy (plain var) reference. Point is still read correctly because of its final fields.
  var racyPoint: Point = _

  // Don't let `this` escape from the constructor, or the final-field guarantee is lost.
  final class Leaky(registry: java.util.List[Leaky]) {
    registry.add(this) // another thread can see this object before `value` is assigned
    val value: Int = 42
  }

  // 2. volatile
  @volatile var published: MutablePoint = _

  // 3. concurrent collections
  val map = new ConcurrentHashMap[String, MutablePoint]()
  val queue = new LinkedBlockingQueue[MutablePoint]()

  // 4. a lock
  private val lock = new Object
  private var guarded: MutablePoint = _

  def plainThreads(): Unit = {
    val producer = thread("producer") {
      racyPoint = new Point(1, 2)
      published = new MutablePoint(3, 4)
      map.put("p", new MutablePoint(5, 6))
      queue.put(new MutablePoint(7, 8))
      lock.synchronized { guarded = new MutablePoint(9, 10) }
    }

    val consumer = thread("consumer") {
      while (published == null) () // spin on the volatile
      val q = queue.take() // blocks until put; put HB take
      val m = map.get("p")
      val g = lock.synchronized(guarded)
      println(s"final fields : ${Option(racyPoint).map(p => s"(${p.x}, ${p.y})")} (may be None: the reference is racy, the fields aren't)")
      println(s"volatile     : (${published.x}, ${published.y})")
      println(s"CHM          : (${m.x}, ${m.y})")
      println(s"queue        : (${q.x}, ${q.y})")
      println(s"lock         : ${Option(g).map(p => s"(${p.x}, ${p.y})")} (None if we locked before the producer did)")
    }

    consumer.start()
    producer.start()
    producer.join()
    consumer.join()
  }

  // Fibers: Deferred.complete HB get returning; Ref.set HB a later Ref.get.
  val fibers: IO[Unit] =
    for {
      d <- Deferred[IO, MutablePoint]
      r <- Ref.of[IO, Option[MutablePoint]](None)
      consumer <- d.get.flatMap(p => IO.println(s"Deferred     : (${p.x}, ${p.y})")).start
      _ <- IO(new MutablePoint(11, 12)).flatMap(d.complete)
      _ <- consumer.join
      _ <- r.set(Some(new MutablePoint(13, 14))).start.flatMap(_.join)
      _ <- r.get.flatMap(p => IO.println(s"Ref          : ${p.map(p => s"(${p.x}, ${p.y})")}"))
    } yield ()

  override def run: IO[Unit] = IO.blocking(plainThreads()) *> fibers
}
