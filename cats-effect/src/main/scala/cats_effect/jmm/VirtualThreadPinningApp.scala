package cats_effect.jmm

import jdk.jfr.consumer.RecordingStream

import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

/** Pinning: a virtual thread that blocks while it can't unmount keeps its carrier thread blocked too.
  *
  * JDK 21-23: blocking inside `synchronized` (or Object.wait) pinned. That was the big Loom gotcha.
  * JDK 24+ (JEP 491): `synchronized` no longer pins. What still pins:
  *   - blocking inside a class initialiser (<clinit>). In Scala, a top-level `object`'s body runs in <clinit>
  *   - blocking while native code is on the stack (JNI or FFM calling back into Java)
  *
  * The demo runs with ONE carrier thread, so pinning is easy to see: pinned blocking gets serialised.
  * `jdk.tracePinnedThreads` was removed in JDK 24; the JFR event `jdk.VirtualThreadPinned` reports pinning now.
  */
object VirtualThreadPinningApp {

  def main(args: Array[String]): Unit = {
    // Must be set before the first virtual thread is created (sbt forks `run` for this module, so the JVM is fresh).
    System.setProperty("jdk.virtualThreadScheduler.parallelism", "1")
    System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "1")

    val pinnedEvents = new AtomicInteger(0)
    val jfr = new RecordingStream()
    jfr.enable("jdk.VirtualThreadPinned").withThreshold(Duration.ZERO)
    jfr.onEvent("jdk.VirtualThreadPinned", _ => pinnedEvents.incrementAndGet())
    jfr.startAsync()

    def timed(label: String)(body: => Unit): Unit = {
      val before = pinnedEvents.get
      val start = System.nanoTime()
      body
      val ms = (System.nanoTime() - start) / 1000000
      Thread.sleep(1500) // JFR delivers events about once a second
      println(f"$label%-45s took $ms%5dms, pinned events = ${pinnedEvents.get - before}")
    }

    // 1000 threads, each holding its own monitor while it sleeps 50ms.
    // Pinned (JDK 21-23): 1 carrier runs them one by one, 1000 x 50ms = 50s.
    // JDK 24+: each unmounts inside synchronized, so they all overlap and finish in about 50ms plus overhead.
    timed("synchronized + sleep, 1000 threads") {
      runAll(1000)(_ => new Object().synchronized(Thread.sleep(50)))
    }

    // Blocking inside an object initialiser still pins: 4 x 200ms run one after another on the single carrier.
    timed("sleep inside object initialiser, 4 threads") {
      val inits: Seq[() => Any] = Seq(() => SlowInit1, () => SlowInit2, () => SlowInit3, () => SlowInit4)
      runAll(4)(i => inits(i)())
    }

    // The same sleep outside an initialiser: no pinning, the 4 sleeps overlap.
    timed("plain sleep, 4 threads") {
      runAll(4)(_ => Thread.sleep(200))
    }

    jfr.close()
  }
}

// Top-level objects: their body runs in the class's static initialiser, which pins a virtual thread that blocks in it.
// Fix: don't block in an object body. Use a lazy val, or do the slow work somewhere explicit.
object SlowInit1 { Thread.sleep(200) }
object SlowInit2 { Thread.sleep(200) }
object SlowInit3 { Thread.sleep(200) }
object SlowInit4 { Thread.sleep(200) }
