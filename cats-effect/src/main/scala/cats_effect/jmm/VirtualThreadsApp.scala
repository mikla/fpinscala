package cats_effect.jmm

import java.lang.management.ManagementFactory
import java.util.concurrent.{ConcurrentHashMap, Executors}
import scala.util.Using

/** Virtual threads (JDK 21+, Project Loom). Java's old "green threads" were dropped in JDK 1.3; virtual threads are
  * the modern version of the idea: cheap threads the JVM schedules onto a small pool of OS "carrier" threads.
  *
  *   - A virtual thread is mounted on a carrier while it runs. When it blocks (sleep, lock, socket I/O, queue take) it
  *     unmounts and the carrier runs something else.
  *   - The carriers are a ForkJoinPool with parallelism = number of cores by default
  *     (`-Djdk.virtualThreadScheduler.parallelism=N`).
  *   - The code is plain blocking code. No callbacks, no IO monad. The JVM does what a fiber runtime does.
  */
object VirtualThreadsApp extends App {

  val threads = ManagementFactory.getThreadMXBean

  // --- 100k threads that each block for 1s finish in about 1s ---
  val N = 100000
  threads.resetPeakThreadCount()
  val start = System.nanoTime()
  Using.resource(Executors.newVirtualThreadPerTaskExecutor()) { exec => // close() waits for all tasks
    (1 to N).foreach(_ => exec.submit((() => Thread.sleep(1000)): Runnable))
  }
  val tookMs = (System.nanoTime() - start) / 1000000
  println(s"$N virtual threads x sleep(1s): took ${tookMs}ms, peak platform threads = ${threads.getPeakThreadCount}")
  // 100k platform threads would need ~100k OS threads; macOS caps a process at a few thousand.

  // --- A virtual thread can resume on a different carrier after it blocks ---
  // toString shows the carrier: VirtualThread[#42,hopper]/runnable@ForkJoinPool-1-worker-3
  val carriers = ConcurrentHashMap.newKeySet[String]()
  val hopper = thread("hopper") {
    (1 to 20).foreach { _ =>
      carriers.add(Thread.currentThread().toString.split('@').last)
      Thread.sleep(5) // unmounts; the scheduler may mount it on any free carrier afterwards
    }
  }
  hopper.start()
  // Other virtual threads competing for the carriers at the same time, so the hopper gets moved around.
  runAll(8)(_ => (1 to 20).foreach(_ => Thread.sleep(5)))
  hopper.join()
  println(s"one virtual thread, 20 sleeps, ran on ${carriers.size} different carriers: $carriers")

  // --- Thread identity ---
  val v = Thread.ofVirtual().unstarted(() => ())
  val p = Thread.ofPlatform().unstarted(() => ())
  println(s"virtual: isVirtual=${v.isVirtual}, isDaemon=${v.isDaemon} (always daemon)")
  println(s"platform: isVirtual=${p.isVirtual}, isDaemon=${p.isDaemon}")
}
