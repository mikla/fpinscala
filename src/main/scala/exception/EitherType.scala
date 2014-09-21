package exception

sealed trait EitherType[+E, +A] {
  def map[B](f: A => B): EitherType[E, B]
  def flatMap[EE >: E, B](f: A => EitherType[EE, B]): EitherType[EE, B]
  def orElse[EE >: E, B >: A](b: => EitherType[EE, B]): EitherType[EE, B]
  def map2[EE >: E, B, C](b: EitherType[EE, B])(f: (A, B) => EitherType[EE, C])
}

case class LeftType[+E](value: E) extends EitherType[E, Nothing] {
  override def map[B](f: (Nothing) => B): EitherType[E, B] = ??? //LeftType(f(value))
  override def map2[EE >: E, B, C](b: EitherType[EE, B])(f: (Nothing, B) => EitherType[EE, C]): Unit = ???
  override def flatMap[EE >: E, B](f: (Nothing) => EitherType[EE, B]): EitherType[EE, B] = ???
  override def orElse[EE >: E, B >: Nothing](b: => EitherType[EE, B]): EitherType[EE, B] = ???
}
case class RightType[+A](value: A) extends EitherType[Nothing, A] {
  override def map[B](f: (A) => B): EitherType[Nothing, B] = RightType(f(value))
  override def map2[EE >: Nothing, B, C](b: EitherType[EE, B])(f: (A, B) => EitherType[EE, C]): Unit = ???
  override def flatMap[EE >: Nothing, B](f: (A) => EitherType[EE, B]): EitherType[EE, B] = ???
  override def orElse[EE >: Nothing, B >: A](b: => EitherType[EE, B]): EitherType[EE, B] = ???
}
