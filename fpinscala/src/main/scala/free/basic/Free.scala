package free.basic

sealed trait Free[F[_], A]
final case class Pure[F[_], A](a: A) extends Free[F, A]
final case class Suspend[F[_], A](s: F[Free[F, A]]) extends Free[F, A]

