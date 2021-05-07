package catsex

object UnusedEqMaybe {

  import cats.Eq

  sealed trait Maybe[+A]
  case class Just[+A](value: A) extends Maybe[A]
  case object None extends Maybe[Nothing]

  object Maybe {

    implicit def eqInstance[A: Eq] = Eq.fromUniversalEquals

  }


}
