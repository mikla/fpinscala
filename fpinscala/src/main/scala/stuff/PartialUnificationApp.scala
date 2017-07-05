package stuff

import cats.Functor
import cats.implicits._

// https://gist.github.com/djspiewak/7a81a395c461fd3a09a6941d4cd040f2
object PartialUnificationApp extends App {

  def foo[F[_], A](fa: F[A]): String = fa.toString

  val func: Function1[Int, Int] = (x: Int) => x * 2

  foo(func) // we partially apply Function1 from the left Function1[Int, _]

  trait Foo[A, B, C]
  def required[F[_], A](f: F[A]) = ()
  required(new Foo[Int, String, Boolean] {})

  def required2[F[_, _], A, B](f: F[A, B]) = ()
  required(new Foo[Int, String, Boolean] {})


  def requiredEither[F[_]: Functor](f: F[Int]) = f.map(_.toString + "!")
  println(requiredEither(Right(3): Either[Int, Int]))

}
