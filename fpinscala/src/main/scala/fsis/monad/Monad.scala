package fsis.monad

import fsis.applicative.Applicative
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