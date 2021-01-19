package jdg.functionalscala.fp_design

import java.time.LocalDate

import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.numeric.{Greater, Less, Positive}
import eu.timepit.refined.string.MatchesRegex
import zio.{Queue, ZIO}
import eu.timepit.refined._
import eu.timepit.refined.types.string.NonEmptyString

object IntroDay1 extends App {

  def shard[R, E, A](
    queue: Queue[A],
    count: Int,
    worked: A => ZIO[R, E, A]) = ???

  case class CardNumber(value: String Refined MatchesRegex[W.`"^4[0-9]{12}(?:[0-9]{3})?$"`.T])
  case class SecurityCode(value: Int Refined Greater[W.`99`.T] And Less[W.`1000`.T])

  case class CreditCard(
    number: CardNumber,
    name: NonEmptyString,
    expirationDate: LocalDate,
    securityCode: SecurityCode)

}
