package laziness

object Ch5Ex extends App {

  def lazyFetch(calc: Boolean, value: => Int): Int = {
    lazy val c = value
    if (calc) c + c else 0
  }

  lazyFetch(true, {
    println("fetching val"); 1 + 40
  })

  val elem1 = () => {println("1"); 1}
  val elem2 = () => {println("2"); 2}
  val elem3 = () => {println("3"); 3}

  Stream(elem1, elem2, elem3)
  val s = Cons(elem1, () => Cons(elem2, () => Cons(elem3, () => Empty)))

  // Infinite streams
  val ones: Stream[Int] = Stream.cons(1, ones)
  val infiniteStream = Stream.from(3).take(3).toList
  val infiniteStreamConstant = Stream.constant(3).take(3).toList

}
