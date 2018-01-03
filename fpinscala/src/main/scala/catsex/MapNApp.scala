package catsex

import cats.implicits._

object MapNApp extends App {

  val x: Either[Int, String] = Right("a")
  val y: Either[Int, String] = Right("b")

  println((x |@| y).map(_ + _))


  val xx: Option[Int] = Some(2)
  val yy: Option[Int] = Some(3)

  (xx |@| yy).map(_ + _)

}
