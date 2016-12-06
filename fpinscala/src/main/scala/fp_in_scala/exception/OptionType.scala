package fp_in_scala.exception

sealed trait OptionType[+A] {
  def map[B](f: A => B): OptionType[B]
  def flatMap[B](f: A => OptionType[B]): OptionType[B]
  def getOrElse[B >: A](default: => B): B
  def orElse[B >: A](ob: => OptionType[B]): OptionType[B]
  def filter(f: A => Boolean): OptionType[A]
}

case class SomeType[+A](x: A) extends OptionType[A] {
  override def map[B](f: (A) => B): OptionType[B] = SomeType(f(x))
  override def flatMap[B](f: (A) => OptionType[B]): OptionType[B] = f(x)
  override def getOrElse[B >: A](default: => B): B = x
  override def orElse[B >: A](ob: => OptionType[B]): OptionType[B] = SomeType(x)
  override def filter(f: (A) => Boolean): OptionType[A] = if (f(x)) SomeType(x) else NoneType
}

object NoneType extends OptionType[Nothing]{
  override def map[B](f: (Nothing) => B): OptionType[B] = NoneType
  override def flatMap[B](f: (Nothing) => OptionType[B]): OptionType[B] = NoneType
  override def getOrElse[B >: Nothing](default: => B): B = default
  override def orElse[B >: Nothing](ob: => OptionType[B]): OptionType[B] = ob
  override def filter(f: (Nothing) => Boolean): OptionType[Nothing] = NoneType
}


