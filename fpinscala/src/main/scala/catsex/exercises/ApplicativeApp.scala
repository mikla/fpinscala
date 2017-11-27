package catsex.exercises

import cats.Applicative
import cats.instances.list._
import cats.instances.option._

object ApplicativeApp extends App {

  // just pure

  (Applicative[List] compose Applicative[Option]).pure(1) // List(Some(1))

}
