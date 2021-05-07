package stuff

import cats.implicits._
import cats.{Order, Semigroupal}

object SameNameImplicitApp extends App {

  implicitly[Semigroupal[Order]]

  val catsSemigroupalForOrder = ""

  def semigroupalEv(implicit ev: Semigroupal[Order]) = "got semigroupal"

  println(semigroupalEv)

  trait Evidence

  object Evidence {
    implicit val evidence = new Evidence {}
  }

  def needsEvidence(implicit ev: Evidence) = "got it"

  val evidence = "breaks compilation"

  println(needsEvidence)

}
