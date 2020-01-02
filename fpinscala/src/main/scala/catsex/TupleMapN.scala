package catsex

import cats.implicits._

object TupleMapN extends App {

  val t4 = (1, 2, "a", "b")
  val t2 = (1, "2")

  val t2_ = t2.map(_ => "")

  println(t2_)

}
