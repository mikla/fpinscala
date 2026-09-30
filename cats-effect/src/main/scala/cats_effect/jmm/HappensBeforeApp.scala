package cats_effect.jmm

/** Each happens-before edge, plus what goes wrong without one. */
object HappensBeforeApp extends App {

  // --- No HB edge: plain var stop flag ---
  // The JIT may hoist `stopPlain` out of the loop, so the reader never sees the write.
  // On HotSpot C2 this loop usually spins forever once it's compiled.
  var stopPlain = false

  val spinner = thread("plain-spinner") {
    var i = 0L
    while (!stopPlain) i += 1
    println(s"plain spinner stopped after $i iterations")
  }
  spinner.start()
  Thread.sleep(500) // let the JIT compile the loop
  stopPlain = true
  spinner.join(2000)
  println(s"plain flag: spinner still alive after 2s = ${spinner.isAlive}")

  // --- volatile write -> later volatile read ---
  // The volatile write also publishes every plain write made before it (`payload` here).
  @volatile var ready = false
  var payload = 0

  val volatileReader = thread("volatile-reader") {
    while (!ready) ()
    println(s"volatile: saw ready, payload = $payload (always 42)")
  }
  volatileReader.start()
  Thread.sleep(500)
  payload = 42
  ready = true // publishes payload too
  volatileReader.join(2000)
  println(s"volatile flag: reader still alive after 2s = ${volatileReader.isAlive}")

  // --- Thread.start(): writes before start() are visible in the new thread ---
  var beforeStart = 0
  beforeStart = 1
  val started = thread("started")(println(s"start: beforeStart = $beforeStart (always 1)"))
  started.start()
  started.join()

  // --- Thread.join(): writes in the thread are visible after join() returns ---
  var writtenInThread = 0
  val worker = thread("joined") { writtenInThread = 7 }
  worker.start()
  worker.join()
  println(s"join: writtenInThread = $writtenInThread (always 7)")

  // --- unlock -> later lock of the SAME monitor ---
  val lock = new Object
  var guarded = 0
  val lockReader = thread("lock-reader") {
    // Each acquire HB-follows the previous release, so this loop is guaranteed to see the write.
    // Locking a DIFFERENT monitor would give no HB edge, and the loop could spin forever like the plain flag.
    while (lock.synchronized(guarded) == 0) ()
    println(s"monitor: guarded = ${lock.synchronized(guarded)} (always 99)")
  }
  lockReader.start()
  Thread.sleep(500)
  lock.synchronized { guarded = 99 }
  lockReader.join(2000)
  println(s"monitor: reader still alive after 2s = ${lockReader.isAlive}")
}
