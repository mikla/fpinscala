import fsis.part1.Functor

import cats.Functor.ops._

object FunctorApp extends App {

  def foo[A](x: A, y: A) = ???
  def bar[F[_], A](x: F[A], y: F[A]) = 1
  bar(1, List(1))

  val listFunctor = implicitly[Functor[List]]
  val optionFunctor = implicitly[Functor[Option]]

  val listOptionFunctor: Functor[Lambda[X => List[Option[X]]]] = listFunctor compose optionFunctor

  val optionsList = List(Some(1), None, Some(2))

  // optionsList map (_ + 1) not compile

  listOptionFunctor.map(optionsList)(_ + 1)

}
