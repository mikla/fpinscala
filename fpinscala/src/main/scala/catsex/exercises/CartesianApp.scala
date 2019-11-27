package catsex.exercises

import cats.Applicative
import cats.implicits._

object CartesianApp extends App {

 println(List(1) *> List(2, 3))

  val x: Option[Int] = Some(1)
  val y: Option[Int] = Some(2)

  implicit val optApplicative: Applicative[Option] = new Applicative[Option] {
    override def pure[A](x: A): Option[A] = ???
    override def ap[A, B](ff: Option[(A) => B])(fa: Option[A]): Option[B] = ???
  }

  (x |@| y).map(_ + _)


}
