package catsex

import cats.data.{Validated, ValidatedNel}
import cats.implicits._

object SequenceUAndPartialUnifiacationExpl extends App {

  val x: List[ValidatedNel[String, Int]] =
    List(Validated.valid(1), Validated.invalid("a"), Validated.invalid("b"))
      .map(_.toValidatedNel)

  x.sequenceU

  x.sequence

}
