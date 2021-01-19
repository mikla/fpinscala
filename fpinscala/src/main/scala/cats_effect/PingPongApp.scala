package cats_effect

import cats.effect.concurrent.Ref
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object PingPong extends IOApp {
  private def print(x: Next): IO[Unit] = IO {
    println(s"${Thread.currentThread.getId}: $x")
  }
  private def go(mine: Next, ref: Ref[IO, Next]): IO[Unit] =
    for {
      access <- ref.access
      (value, setter) = access
      _ <-
        if (value == mine) print(mine) *> setter(mine.other)
        else setter(value) *> go(mine, ref)
    } yield ()

  sealed trait Next {
    def other: Next
  }

  case object Ping extends Next {
    val other: Next = Pong
  }

  case object Pong extends Next {
    val other: Next = Ping
  }
  override def run(args: List[String]): IO[ExitCode] = for {
    ref <- Ref.of[IO, Next](Ping)
    pingFiber <- go(Ping, ref).foreverM.start
    pongFiber <- go(Pong, ref).foreverM.start
    _ <- (pingFiber.join :: pongFiber.join :: Nil).sequence
  } yield ExitCode.Success
}
