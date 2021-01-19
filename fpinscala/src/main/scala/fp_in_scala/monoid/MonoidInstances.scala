package fp_in_scala.monoid

object MonoidInstances {

  val stringMonoid = new Monoid[String] {
    override def op(a: String, b: String): String = a + b
    override def zero: String = ""
  }

  def listMonoid[A] = new Monoid[List[A]] {
    override def op(a: List[A], b: List[A]): List[A] = a ++ b
    override def zero: List[A] = Nil
  }

  val intAddition: Monoid[Int] = new Monoid[Int] {
    override def op(a: Int, b: Int): Int = a + b
    override def zero: Int = 0
  }

  val intMultiplication: Monoid[Int] = new Monoid[Int] {
    override def op(a: Int, b: Int): Int = a * b
    override def zero: Int = 1
  }

  val booleanOr: Monoid[Boolean] = new Monoid[Boolean] {
    override def op(a: Boolean, b: Boolean): Boolean = a || b
    override def zero: Boolean = false
  }

  val booleanAnd: Monoid[Boolean] = new Monoid[Boolean] {
    override def op(a: Boolean, b: Boolean): Boolean = a && b
    override def zero: Boolean = true
  }

  def optionMonoid[A] = new Monoid[Option[A]] {
    override def op(a: Option[A], b: Option[A]): Option[A] = a.orElse(b)
    override def zero: Option[A] = None
  }

  def endoMonoid[A]: Monoid[A => A] = new Monoid[(A) => A] {
    override def op(a: A => A, b: A => A): A => A = a.compose(b)
    override def zero: A => A = identity
  }

  def flip[A](monoid: Monoid[A]): Monoid[A] = new Monoid[A] {
    override def op(a: A, b: A): A = monoid.op(b, a)
    override def zero: A = monoid.zero
  }

}
