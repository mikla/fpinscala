package catsex

import cats.Monad
import cats.implicits._

object ifMApplication extends App {

  case class Action1()

  case class Action2()

  println {
    List(
      Action1().some.whenA(1 == 2),
      Action2().some.whenA(1 == 1)
    )
  }

  def log[F[_] : Monad] = for {
    _ <- println("logger").pure[F].whenA(true)
  } yield ()

  log[Option]

}
