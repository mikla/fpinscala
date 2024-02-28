package cats_effect.galvan

import cats.effect.{ExitCode, IO, IOApp}

import scala.Console._
import scala.io.StdIn

object CE3ApiRecapApp extends IOApp {

  def echoForever: IO[Nothing] = (for {
    s <- IO.delay(StdIn.readLine())
    _ <- IO.delay(println(s))
  } yield ()).foreverM

  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- IO.delay(println("ok"))
    _ <- echoForever
  } yield ExitCode.Success
}
