package stuff

import java.util.UUID

import cats.implicits._
import catsex.exercises.ValidatedApp.{CommandFailed, CommandOk, CommandProcessingResult}

object EitherApp extends App {

  val proto: Option[String] = Some("Str")

  def deserialize(str: String): Either[Throwable, String] = Right(str)

  def traverse: Either[Throwable, Option[String]] =
    proto.map(str => deserialize(str)).sequence[Either[Throwable, ?], String]

  val x: Option[Int] = None

  println(x.forall(_ == 1))

  case class InvalidRequest(reason: String)

  def validate(condition: Boolean, onError: String) = Either.cond(condition, Right(()), InvalidRequest(onError))

  val y = 5

  val xx = for {
    _ <- validate(y > 0, "Must be positive")
    _ <- validate(y < 10, "??")
  } yield ()

  println(xx)

  val eitherFlatMap: Either[String, Int] = Right(1)
  println(eitherFlatMap *> Right(2))

  (2, 3).tupleLeft()

  eitherFlatMap.fold(_ => (), a => ())

  val someOption: Option[Int] = None
  someOption.as(println("not lazy"))

  val processed: List[Either[String, Int]] = List(Right(1), Left("error"))

  println(processed.partition(_.isRight))
  println(processed.flatMap(_.toOption))

}
