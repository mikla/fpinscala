package exception

sealed trait EitherType[+E, +A]
case class LeftType[+E](value: E) extends EitherType[E, Nothing]
case class RightType[+A](value: A) extends EitherType[Nothing, A]
