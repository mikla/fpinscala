package catsex

import cats.implicits._
import cats.{Applicative, Foldable, Id}

object GenericSumApp extends App {

  abstract class Summator[F[_] : Applicative : Foldable] {
    def sum(a: F[Int], b: F[Int]): Int =
      a.foldLeft(0)((_, a1) => b.foldLeft(0)((_, b1) => a1 + b1))

    def mult(a: F[Int], b: F[Int]): Int =
      (a, b).mapN(_ * _).fold

  }

  object IdSummator extends Summator[Id]
  object OptionSummator extends Summator[Option]

  println(IdSummator.sum(3, 5)) // 8
  println(OptionSummator.sum(Some(5), Some(5))) // 10
  println(OptionSummator.sum(Some(5), None)) // 0

  // mult
  println(IdSummator.mult(3, 5)) // 8
  println(OptionSummator.mult(Some(5), Some(5))) // 10
  println(OptionSummator.mult(Some(5), None)) // 0
}
