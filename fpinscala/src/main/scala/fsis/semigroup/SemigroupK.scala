package fsis.semigroup

import simulacrum.{op, typeclass}

import scala.language.higherKinds

@typeclass trait SemigroupK[F[_]] {
  self =>

  @op("<+>") def combine[A](x: F[A], y: => F[A]): F[A]

  def toSemigroup[A]: Semigroup[F[A]] = new Semigroup[F[A]] {
    override def combine(a: F[A], b: F[A]): F[A] = self.combine(a, b)
  }

}

object SemigroupK {

  val listSmk = new SemigroupK[List] {
    override def combine[A](x: List[A], y: => List[A]): List[A] = x ++ y
  }

  val optionSmk = new SemigroupK[Option] {
    override def combine[A](x: Option[A], y: => Option[A]): Option[A] = x.orElse(y)
  }

}
