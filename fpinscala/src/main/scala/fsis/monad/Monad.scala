package fsis.monad

import fsis.applicative.Applicative
import fsis.monoid.Monoid
import simulacrum.typeclass


@typeclass trait Monad[F[_]] extends Applicative[F] {
  self =>

  def pure[A](a: A): F[A]

  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  override def apply[A, B](fa: F[A])(ff: F[A => B]): F[B] =
    flatMap(ff)(f => map(fa)(f))
  //    flatMap(fa)(a => flatMap(ff)(f => pure(f(a)))) // this could be implementation as well?

  def flatten[A](ffa: F[F[A]]): F[A] =
    flatMap(ffa)(identity)

  override def map[A, B](fa: F[A])(f: A => B): F[B] =
    flatMap(fa)(a => pure(f(a)))

  def componse[G[_]](implicit G: Monad[G]): Monad[Lambda[X => F[G[X]]]] = new Monad[Lambda[X => F[G[X]]]] {
    override def pure[A](a: A): F[G[A]] = self.pure(G.pure(a))
    override def flatMap[A, B](fga: F[G[A]])(f: A => F[G[B]]): F[G[B]] = {
      val nested = self.map(fga)(ga => G.map(ga)(a => f(a))): F[G[F[G[B]]]]
      flatten(nested) // it's compiles. But it is wrong. We can't have monad compositon in general
    }
  }

  // but we don't have such possibility to call empty
  // we can try to require Monoid[F[A]]
  // this is fine when you work with particular types.
  // but in deeper sense - this is not useful.
  def filter1[A](fa: F[A])(predicate: A => Boolean)(implicit M: Monoid[F[A]]): F[A] =
    flatMap(fa)(a => if (predicate(a)) fa else M.empty)

}

object Monad {

  implicit val listMonad: Monad[List] = new Monad[List] {
    override def pure[A](a: A): List[A] = List(a)
    override def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = fa.flatMap(f)
  }

  implicit val optionMonad: Monad[Option] = new Monad[Option] {
    override def pure[A](a: A): Option[A] = Option(a)
    override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa.flatMap(f)
  }

}

trait MonadLaws[F[_]] {

  import Monad.ops._

  implicit def M: Monad[F]

  def flatMapAssoc[A, B, C](fa: F[A], f: A => F[B], g: B => F[C]) =
    fa.flatMap(f).flatMap(g) == fa.flatMap(a => f(a).flatMap(g))

  def leftIdentity[A, B](a: A, f: A => F[B]) =
    M.pure(a).flatMap(f) == f(a)

  def rightIdentity[A, B](fa: F[A]) =
    fa.flatMap(a => M.pure(a)) == fa
}
