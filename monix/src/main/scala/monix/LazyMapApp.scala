package monix

import monix.eval.Coeval

object LazyMapApp extends App {

  lazy val value1 = {
    println(1)
    1
  }

  lazy val value2 = {
    println(2)
    2
  }

  lazy val value3 = {
    println(3)
    3
  }

  lazy val m = Map(("1", () => value1), ("2", () => value2), ("3", () => value3))

  println(m("1")())

  println(m("2")())

  // With monix

  val value1m = Coeval.evalOnce {
    println("value 1 eval")
    1
  }

  val value2m = Coeval.evalOnce {
    println("value 2 eval")
    2
  }

  val value3m = Coeval.evalOnce {
    println("value 3 eval")
    3
  }

  val lazyMap = Map("1" -> value1m, "2" -> value2m, "3" -> value3m)

  println(lazyMap("1").value)
  println(lazyMap("2").value)

}
