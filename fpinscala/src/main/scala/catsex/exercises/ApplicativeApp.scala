package catsex.exercises

import cats.{Applicative, Apply, Semigroupal}
import cats.instances.list._
import cats.instances.option._

object ApplicativeApp extends App {

  // just pure

  Applicative[List].compose(Applicative[Option]).pure(1) // List(Some(1))

  Applicative[List].product(List(1), List(2, 3)) // List((1,2), (1,3))

  println(List(1) ** List(2, 3))

  implicit class ProductExtensionOps[F[_] : Semigroupal, A](fa: F[A]) {
    def **[B](fb: F[B])(implicit A: Semigroupal[F]): F[(A, B)] = A.product(fa, fb)
  }

}
