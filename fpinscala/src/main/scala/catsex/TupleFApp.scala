package catsex

import cats.{Applicative, Id, Monad}
import cats.implicits._

object TupleFApp extends App {

  def c[F[_] : Monad, A, B](c: F[(A, B)]): (F[A], F[B]) =
    (c.map(_._1), c.map(_._2))

}
