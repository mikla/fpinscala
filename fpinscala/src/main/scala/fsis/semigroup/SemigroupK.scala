package fsis.semigroup

trait SemigroupK[F[_]] {
  self =>

  def combine[A](x: F[A], y: => F[A]): F[A]

  def toSemigroup[A]: Semigroup[F[A]] = new Semigroup[F[A]] {
    override def combine(a: F[A], b: F[A]): F[A] = self.combine(a, b)
  }

}

object SemigroupK {

  val listSmk: SemigroupK[List] = new SemigroupK[List] {
    override def combine[A](x: List[A], y: => List[A]): List[A] = x ++ y
  }

  val optionSmk: SemigroupK[Option] = new SemigroupK[Option] {
    override def combine[A](x: Option[A], y: => Option[A]): Option[A] = x.orElse(y)
  }

}
