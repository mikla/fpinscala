package cats_effect

import cats.effect.IO
import cats.effect.unsafe.implicits.global

object UnitLogging extends App {

  class Live {
    def process: IO[Unit] = {
      println("log from process")
      IO.delay(println("log from IO"))
    }
  }

  val live = new Live
  live.process // IO
  live.process // IO

  live.process.unsafeRunSync()

  import cats.effect.unsafe.implicits.global
  import cats.effect.syntax.all._
  import cats.syntax.all._
  import cats.data.EitherT
  import cats.effect.IO
  (for {
    _ <- EitherT.left[Unit](IO(new RuntimeException("ee"))).start
    _ <- EitherT.right[Throwable](IO(println("2222"))).start
  } yield ()).value.unsafeRunSync()

}
