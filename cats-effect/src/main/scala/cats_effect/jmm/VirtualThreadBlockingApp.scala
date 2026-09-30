package cats_effect.jmm

import cats.effect.{IO, IOApp, Resource}
import cats.syntax.all._

import java.lang.management.ManagementFactory
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

/** cats-effect fibers vs virtual threads.
  *
  * Both are cheap, JVM-scheduled "threads". They solve blocking differently:
  *   - IO.blocking moves the fiber to CE's blocking pool, a cached pool of PLATFORM threads. N concurrent blocking calls
  *     means up to N OS threads.
  *   - Since CE 3.6, IO.blocking on a fiber that's already running on a virtual thread blocks in place. With `evalOn` a
  *     virtual-thread executor, a blocking call only costs a virtual thread.
  *
  * Use this for blocking APIs you can't avoid (JDBC, legacy SDKs). Code that's already async (IO.sleep, http4s,
  * fs2) doesn't block anything and doesn't need it.
  */
object VirtualThreadBlockingApp extends IOApp.Simple {

  val N = 2000 // keep it modest: the default path really creates ~N OS threads
  val threads = ManagementFactory.getThreadMXBean

  val virtualThreadEc: Resource[IO, ExecutionContext] =
    Resource
      .make(IO(Executors.newVirtualThreadPerTaskExecutor()))(e => IO.blocking(e.close()))
      .map(ExecutionContext.fromExecutorService)

  def blockingCall(i: Int): IO[Unit] = IO.blocking(Thread.sleep(200))

  def measure(label: String)(work: IO[Unit]): IO[Unit] =
    for {
      _ <- IO(threads.resetPeakThreadCount())
      before <- IO(threads.getThreadCount)
      took <- work.timed.map(_._1)
      peak <- IO(threads.getPeakThreadCount)
      _ <- IO.println(f"$label%-36s took ${took.toMillis}%4dms, extra platform threads at peak = ${peak - before}")
    } yield ()

  // Virtual threads first: the blocking pool keeps its idle threads alive for 60s, which would skew the second run.
  override def run: IO[Unit] =
    virtualThreadEc.use { vt =>
      measure(s"IO.blocking x $N (virtual threads)")((1 to N).toList.parTraverse_(i => blockingCall(i).evalOn(vt)))
    } *>
      measure(s"IO.blocking x $N (blocking pool)")((1 to N).toList.parTraverse_(blockingCall))
}
