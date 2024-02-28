package catsex

import cats.implicits._

object TupleMapN extends App {

  val t4 = (1, 2, "a", "b")
  val t2 = (1, "2")

  val t2_ = t2.map(_ => "")

  val opt1: Option[String] = Some("1")
  val opt2: Option[String] = Some("2")

  println((opt1, opt2).mapN((_, _) => true).getOrElse(false))

  println(t2_)

}
