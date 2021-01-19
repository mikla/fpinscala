package cats_effect.io

import cats.effect.{IO, SyncIO}
import cats.implicits._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.io.StdIn

object IOTimerApp extends App {

  implicit val timer = IO.timer(ExecutionContext.global)

  val io: IO[Unit] = IO.sleep(2.seconds) *> IO(println("Hello!"))

//  val cancel: IO[Unit] =
//    io.unsafeRunCancelable(r => println(s"Done: $r"))
//
//  cancel.unsafeRunSync()

  val pureResult: SyncIO[IO[Unit]] = io.runCancelable(r => IO(println(s"Done: $r")))

  // in example they use .flatten.
  // and it doesn't work
  pureResult.toIO.unsafeRunCancelable {
    case Right(l) => println("cb: " + l)
    case Left(e) => println("cb: " + e)
  }

  StdIn.readLine()

}
