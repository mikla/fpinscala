package cats_effect.jmm

import cats.effect.std.Mutex
import cats.effect.{IO, IOApp}
import cats.syntax.all._

import scala.concurrent.duration._

/** synchronized = mutual exclusion + visibility. It's reentrant and it BLOCKS the thread that waits for it.
  *
  * Virtual threads: on JDK 21-23 a virtual thread blocked inside `synchronized` pins its carrier OS thread. JEP 491
  * (JDK 24+) removes that, so on this project's JDK 25 `synchronized` is fine for virtual threads (see
  * VirtualThreadPinningApp for what still pins).
  *
  * cats-effect fibers are a different story: they run on a fixed pool of platform threads, and a fiber blocked in
  * `synchronized` holds a whole worker thread on any JDK. That's what this app shows.
  */
object SynchronizedApp extends IOApp.Simple {

  // --- Reentrancy: the thread that holds a monitor can lock it again ---
  object Account {
    private var balance = 100

    def withdraw(n: Int): Unit = synchronized {
      if (balance >= n) balance -= n
    }

    def withdrawTwice(n: Int): Unit = synchronized {
      withdraw(n) // locks `this` again; works because the monitor is reentrant
      withdraw(n)
    }

    def get: Int = synchronized(balance) // also lock the read, for visibility
  }

  // --- Blocking vs semantic blocking ---
  val lock = new Object

  // Each fiber holds a monitor while it sleeps. The waiters block real compute threads, so throughput is limited by
  // the size of the pool, and other fibers get starved. Never do this on the compute pool.
  def monitorWork(i: Int): IO[Unit] =
    IO(lock.synchronized(Thread.sleep(50)))

  // Mutex suspends the waiting fiber instead of blocking a thread, so the worker thread is free to run other fibers.
  def mutexWork(mutex: Mutex[IO])(i: Int): IO[Unit] =
    mutex.lock.surround(IO.sleep(50.millis))

  // Runs `work` while a separate fiber ticks every 10ms. Returns (elapsed, ticks). Few ticks = starved pool.
  def withHeartbeat(work: IO[Unit]): IO[(FiniteDuration, Int)] =
    for {
      ticks <- IO.ref(0)
      elapsed <- (IO.sleep(10.millis) *> ticks.update(_ + 1)).foreverM.background.surround(work.timed.map(_._1))
      n <- ticks.get
    } yield (elapsed, n)

  val Fibers = 20

  override def run: IO[Unit] =
    for {
      _ <- IO(Account.withdrawTwice(30))
      _ <- IO.println(s"reentrant withdrawTwice(30): balance = ${Account.get}")

      cpus <- IO(Runtime.getRuntime.availableProcessors)
      _ <- IO.println(s"compute pool size ~ $cpus, running $Fibers fibers that each hold the lock for 50ms")

      // Expect ~1000ms either way (the lock serialises 20 x 50ms). The difference is the heartbeat: with
      // synchronized every compute thread can end up blocked on the monitor, so the ticker barely runs.
      sync <- withHeartbeat((1 to Fibers).toList.parTraverse_(monitorWork))
      _ <- IO.println(s"synchronized: took ${sync._1.toMillis}ms, heartbeat ticks = ${sync._2}")

      mutex <- Mutex[IO]
      mtx <- withHeartbeat((1 to Fibers).toList.parTraverse_(mutexWork(mutex)))
      _ <- IO.println(s"Mutex:        took ${mtx._1.toMillis}ms, heartbeat ticks = ${mtx._2} (~elapsed / 10ms)")
    } yield ()
}
