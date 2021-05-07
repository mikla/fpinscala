package catsex

import alleycats.std.all._
import cats.data.{Validated, ValidatedNel}
import cats.implicits._
import model.Employee
import shapelessex.application.UserId

object SequenceUAndPartialUnifiacationExpl extends App {

  val x: List[ValidatedNel[String, Int]] =
    List(Validated.valid(1), Validated.invalid("a"), Validated.invalid("b"))
      .map(_.toValidatedNel)

  x.sequence

  val deserializedResult: Map[UserId, Either[Throwable, Employee]] =
    Map(UserId("id") -> Right(Employee("name", 1, manager = false)))

  val map = deserializedResult.sequence

  println(map)
}
