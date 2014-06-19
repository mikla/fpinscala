package exception

sealed trait Option[+A] {
  def map[B](f: A => B): Option[B]
  def flatMap[B](f: A => Option[B]): Option[B]
  def getOrElse[B >: A](default: => B): B
  def orElse[B >: A](ob: => Option[B]): Option[B]
  def filter(f: A => Boolean): Option[A]
}

case class Some[+A](x: A) extends Option[A] {
  override def map[B](f: (A) => B): Option[B] = Some(f(x))
  override def flatMap[B](f: (A) => Option[B]): Option[B] = f(x)
}

object None extends Option[Nothing]{
  override def filter(f: (Nothing) => Boolean): Option[Nothing] = ???
  override def orElse[B >: Nothing](ob: => Option[B]): Option[B] = ???
  override def getOrElse[B >: Nothing](default: => B): B = ???
  override def flatMap[B](f: (Nothing) => Option[B]): Option[B] = ???
  override def map[B](f: (Nothing) => B): Option[B] = ???
}


