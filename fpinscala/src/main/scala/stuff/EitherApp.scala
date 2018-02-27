package stuff

import java.util.UUID

import cats.implicits._
import catsex.exercises.ValidatedApp.{CommandFailed, CommandOk, CommandProcessingResult}

object EitherApp extends App {

  val proto: Option[String] = Some("Str")

  def deserialize(str: String): Either[Throwable, String] = Right(str)

  def traverse: Either[Throwable, Option[String]] = {
    proto.map(str => deserialize(str)).sequence[Either[Throwable, ?], String]
  }


  val x: Option[Int] = None

  println(x.forall(_ == 1))

}
