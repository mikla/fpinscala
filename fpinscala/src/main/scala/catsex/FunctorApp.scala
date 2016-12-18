package catsex

import cats.Functor
import cats.implicits._

object FunctorApp extends App {

  val source = List("cat", "is")
  val product = Functor[List].fproduct(source)(_.length).toMap
  println(product) // Map(cat -> 3, is -> 2)

  // functor composition

  Functor[List] compose Functor[Option]

}
