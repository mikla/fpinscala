package cats_effect.jmm

import java.util.concurrent.atomic.AtomicInteger
import scala.jdk.CollectionConverters._

/** Double-checked locking needs the field to be volatile.
  *
  * `instance = new Heavy()` is allocate, run constructor, assign. Without volatile, the JIT or CPU may reorder the
  * assignment before the constructor writes. A thread on the fast path (the first check, which takes no lock) can then
  * see a non-null reference to a half-constructed object. That thread never takes the lock, so it has no HB edge with
  * the writer.
  *
  * This is very hard to see on x86 (TSO). It shows up on ARM and under jcstress. The broken version is here to read,
  * not to reproduce.
  */
object DoubleCheckedLockingApp extends App {

  val constructions = new AtomicInteger(0)

  class Heavy {
    var x: Int = 0 // a non-final field is exactly what can be seen half-initialised
    constructions.incrementAndGet()
    x = 42
  }

  // BROKEN: no volatile, so no HB edge on the fast path.
  object BrokenHolder {
    private var instance: Heavy = _

    def get: Heavy = {
      if (instance == null) { // fast path, no lock
        synchronized {
          if (instance == null) instance = new Heavy // publish can be reordered before x = 42
        }
      }
      instance // may see x == 0
    }
  }

  // CORRECT: the volatile write in the slow path HB the volatile read on the fast path.
  object VolatileHolder {
    @volatile private var instance: Heavy = _

    def get: Heavy = {
      val local = instance // read the volatile once
      if (local != null) local
      else
        synchronized {
          if (instance == null) instance = new Heavy
          instance
        }
    }
  }

  // IDIOMATIC: lazy val does double-checked locking for you.
  //   Scala 2.13: volatile bitmap + synchronized on the enclosing object.
  //   Scala 3.3+: lock-free CAS on a per-field state, so it no longer locks the whole enclosing object.
  //   In 2.13, two objects whose lazy vals initialise each other from different threads can deadlock,
  //   and a slow initialiser blocks every other synchronized call on the same object.
  object LazyHolder {
    lazy val instance: Heavy = new Heavy
  }

  def race(name: String)(get: => Heavy): Unit = {
    constructions.set(0)
    val seen = new java.util.concurrent.ConcurrentLinkedQueue[Int]()
    runAll(16)(_ => seen.add(get.x))
    val bad = seen.asScala.count(_ != 42)
    println(f"$name%-16s constructions = ${constructions.get}, half-initialised reads = $bad")
  }

  race("broken DCL")(BrokenHolder.get) // almost always 1 / 0 on x86, but the JMM doesn't promise that
  race("volatile DCL")(VolatileHolder.get)
  race("lazy val")(LazyHolder.instance)
}
