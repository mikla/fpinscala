package mtl

import java.time.LocalDate

import cats.MonadError
import cats.implicits._
import cats.data.EitherT
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

// Domain definition

case class Row(id: Int, name: Option[String], start: LocalDate, end: LocalDate, serializedMap: Option[String])

case class DatePeriod(start: LocalDate, end: LocalDate)

case class Program(id: Int, name: String, datePeriod: DatePeriod, map: Map[String, String])

case class Command(program: Program)

object GettingRidOfMonadTransformersApp extends App {

  type Effect[T] = Either[Throwable, T]

  val testRow = Row(1, Some("Name"), LocalDate.now(), LocalDate.now, Some("""{"key":"value"}"""))

  def programToCommand(row: Row): Either[Throwable, Command] = {
    val entry = for {
      period <- EitherT.fromEither[Option](periodFromBounds(row.start, row.end))
      name <- EitherT(row.name.map(_.pure[Effect]))
      m <- EitherT(row.serializedMap.map(sm => tryDeserialize(sm)))
    } yield Program(row.id, name, period, m)

    EitherT(entry.value).map(entry => Command(entry))
      .value.getOrElse(Left(new Exception("Visi ir slikti")))

  }

  def programToCommandTF[F[_]](row: Row)(implicit M: MonadError[F, Throwable]): F[Command] = {
    for {
      period <- periodFromBoundsTF[F](row.start, row.end)
      name <- row.name.map(_.pure[F]).getOrElse(M.raiseError(new Exception("name ?")))
      m <- row.serializedMap.map(sm => tryDeserializeTF[F](sm))
        .getOrElse(M.raiseError(new Exception("serialized map ?")))
    } yield Command(Program(row.id, name, period, m))
  }

  println(programToCommand(testRow))
  println(programToCommandTF[Effect](testRow))

  private def tryDeserialize(m: String): Either[Throwable, Map[String, String]] =
    decode[Map[String, String]](m).leftMap(_.fillInStackTrace())

  private def tryDeserializeTF[F[_]](m: String)(
    implicit M: MonadError[F, Throwable]): F[Map[String, String]] =
    decode[Map[String, String]](m) match {
      case Right(v) => M.pure(v)
      case Left(err) => M.raiseError(err)
    }

  private def periodFromBounds(start: LocalDate, end: LocalDate): Either[Throwable, DatePeriod] =
    Right(DatePeriod(start, end))

  private def periodFromBoundsTF[F[_]](start: LocalDate, end: LocalDate)(
    implicit M: MonadError[F, Throwable]): F[DatePeriod] = M.pure(DatePeriod(start, end))

}
