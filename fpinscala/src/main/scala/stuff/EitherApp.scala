package stuff

import cats.syntax.traverse._
import cats.instances.option._
import cats.instances.either._

object EitherApp extends App {

  val proto: Option[String] = Some("Str")

  def deserialize(str: String): Either[Throwable, String] = Right(str)

  def traverse: Either[Throwable, Option[String]] = {
    proto.map(str => deserialize(str)).sequence[Either[Throwable, ?], String]

  }

}
