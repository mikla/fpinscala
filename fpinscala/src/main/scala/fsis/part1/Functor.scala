package fsis.part1

import scala.language.{reflectiveCalls, higherKinds}
import simulacrum._

@typeclass trait Functor[F[_]] { self =>
  def map[A, B](fa: F[A])(f: A => B): F[B]

  def lift[A, B](f: A => B): F[A] => F[B] = fa => map(fa)(f)

  def compose[G[_]](implicit G: Functor[G]): Functor[({type l[X] = F[G[X]]})#l] = new Functor[({type l[X] = F[G[X]]})#l] {
    override def map[A, B](fga: F[G[A]])(f: A => B): F[G[B]] = self.map(fga)(ga => G.map(ga)(f))
  }

  // just rewritten with kind-projection
  def compose1[G[_]](implicit G: Functor[G]): Functor[Lambda[X => F[G[X]]]] = new Functor[Lambda[X => F[G[X]]]] {
    override def map[A, B](fga: F[G[A]])(f: (A) => B): F[G[B]] =
      self.map(fga)(ga => G.map(ga)(f))
  }

}

trait FunctorLaws {

  def identity[F[_], A](fa: F[A])(implicit f: Functor[F]) = f.map(fa)(a => a) == fa

  def composition[F[_], A, B, C](fa: F[A])(f: A => B)(g: B => C)(implicit F: Functor[F]) =
    F.map(F.map(fa)(f))(g) == F.map(fa)(f andThen g)

}

object Functor {

  implicit val listFunctor: Functor[List] = new Functor[List] {
    override def map[A, B](fa: List[A])(f: (A) => B): List[B] = fa.map(f)
  }

  implicit val optionFunctor: Functor[Option] = new Functor[Option] {
    override def map[A, B](fa: Option[A])(f: (A) => B): Option[B] = fa.map(f)
  }

  implicit def function1Functor[X]: Functor[X => ?] = new Functor[X => ?] {
    override def map[A, B](fa: X => A)(f: (A) => B): X => B = fa andThen f
  }

  def function1Functor2[X]: Functor[Lambda[X1 => Function1[X, X1]]] = new Functor[Lambda[X1 => Function1[X, X1]]] {
    override def map[A, B](fa: (X) => A)(f: (A) => B): (X) => B = ???
  }

}
