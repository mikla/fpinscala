package cats_effect.jmm

import cats.effect.{IO, IOApp, Ref}
import cats.syntax.all._

import java.util.concurrent.atomic.AtomicInteger

/** volatile = visibility + ordering, NOT atomicity.
  *
  * `count += 1` is read, add, write. Two threads can read the same value and both write value + 1, so one update is
  * lost. volatile only makes sure each read sees the latest write.
  */
object VolatileNotAtomicApp extends IOApp.Simple {

  val Threads = 8
  val PerThread = 100000
  val Expected = Threads * PerThread

  @volatile var volatileCount = 0
  val atomicCount = new AtomicInteger(0)
  var syncCount = 0
  val lock = new Object

  def plainThreads(): Unit = {
    runAll(Threads)(_ => (1 to PerThread).foreach(_ => volatileCount += 1)) // race: lost updates
    runAll(Threads)(_ => (1 to PerThread).foreach(_ => atomicCount.incrementAndGet())) // CAS
    runAll(Threads)(_ => (1 to PerThread).foreach(_ => lock.synchronized(syncCount += 1))) // mutual exclusion

    println(s"expected           = $Expected")
    println(s"@volatile count++  = $volatileCount (usually less)")
    println(s"AtomicInteger      = ${atomicCount.get}")
    println(s"synchronized       = $syncCount")
  }

  // cats-effect: Ref is an AtomicReference underneath, and `update` is a CAS retry loop.
  val refCounter: IO[Int] =
    for {
      ref <- Ref.of[IO, Int](0)
      _ <- (1 to Threads).toList.parTraverse_(_ => ref.update(_ + 1).replicateA_(PerThread))
      n <- ref.get
    } yield n

  // Anti-pattern: get then set is two separate atomic steps, so this races just like volatile count++.
  val refGetSet: IO[Int] =
    for {
      ref <- Ref.of[IO, Int](0)
      _ <- (1 to Threads).toList.parTraverse_(_ => (ref.get.flatMap(n => ref.set(n + 1))).replicateA_(PerThread))
      n <- ref.get
    } yield n

  override def run: IO[Unit] =
    IO.blocking(plainThreads()) *>
      refCounter.flatMap(n => IO.println(s"Ref.update         = $n")) *>
      refGetSet.flatMap(n => IO.println(s"Ref.get + Ref.set  = $n (usually less)"))
}
