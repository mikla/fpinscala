package stuff

sealed trait Free[F[_], A]
final case class Join[F[_], A](s: F[Free[F, A]]) extends Free[F, A]
final case class Point[F[_], A](a: A) extends Free[F, A]
