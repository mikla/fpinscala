package fsis.traversable

import fsis.applicative.Applicative
import simulacrum.typeclass


@typeclass trait Traversable[F[_]] {
  def traverse[G[_] : Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]
  def sequence[G[_] : Applicative, A](fga: F[G[A]]): G[F[A]] =
    traverse(fga)(identity)
}

object Traversable {


  implicit val listTraversable: Traversable[List] = new Traversable[List] {
    override def traverse[G[_] : Applicative, A, B](fa: List[A])(f: A => G[B]): G[List[B]] =
      ???
  }

}

/*
  List(1, 2).traverse { v => Some(v) } == Some(List(1, 2))
 */
