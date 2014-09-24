package laziness

object Ch5Ex extends App {

  def lazyFetch(calc: Boolean, value: => Int): Int = {
    lazy val c = value
    if (calc) c + c else 0
  }

  lazyFetch(true, {
    println("fetching val"); 1 + 40
  })

}
