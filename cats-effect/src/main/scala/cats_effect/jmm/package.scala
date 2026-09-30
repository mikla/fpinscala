package cats_effect

/** Java Memory Model examples.
  *
  * Happens-before (HB) is the only guarantee the JMM gives. A write is guaranteed visible to a read only if the write
  * happens-before the read. HB edges:
  *   - unlock of a monitor -> every later lock of the same monitor
  *   - volatile write -> every later read of the same variable
  *   - Thread.start() -> every action in the started thread
  *   - every action in a thread -> Thread.join() returning on that thread
  *   - writes to final fields in a constructor -> any thread that sees the (properly published) reference
  *
  * Results of the racy examples depend on JIT and CPU (x86 is much stronger than ARM), so "can fail" means the JMM allows
  * it, not that you'll see it on every run. Use jcstress to test these properly.
  *
  * The examples use virtual threads (JDK 21+). The JMM is exactly the same for virtual and platform threads: the HB
  * edges above apply to both. What differs is scheduling (see VirtualThreadsApp and VirtualThreadPinningApp).
  */
package object jmm {

  /** Unstarted virtual thread. Virtual threads are always daemon, so a racy demo that spins forever won't keep the JVM
    * alive. A virtual thread that busy-spins never yields, though, so it holds one carrier thread the whole time.
    */
  def thread(name: String)(body: => Unit): Thread =
    Thread.ofVirtual().name(name).unstarted(() => body)

  def platformThread(name: String)(body: => Unit): Thread =
    Thread.ofPlatform().name(name).daemon(true).unstarted(() => body)

  def runAll(n: Int)(body: Int => Unit): Unit = {
    val ts = (0 until n).map(i => thread(s"worker-$i")(body(i)))
    ts.foreach(_.start())
    ts.foreach(_.join())
  }

}
