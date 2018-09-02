package catsex.api

import cats.implicits._
import cats.kernel.PartialOrder

object PartialOrderApp extends App {

  println(PartialOrder[Int].pmax(1, 2))
  println(PartialOrder[Int].pmin(1, 2))

  (1, 2).pmin(1, 2)

}
