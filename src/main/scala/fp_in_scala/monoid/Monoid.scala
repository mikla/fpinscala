package fp_in_scala.monoid

trait Monoid[A] {
  def op(a: A, b: A): A
  def zero: A
}

// laws:
// op(op(a, b), c) == op(a, op(b, c))
// op(zero, a) == op(a, zero)

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
    override def op(a: Option[A], b: Option[A]): Option[A] = a orElse b
    override def zero: Option[A] = None
  }

  def endoMonoid[A]: Monoid[A => A] = new Monoid[(A) => A] {
    override def op(a: A => A, b: A => A): A => A = a compose b
    override def zero: A => A = identity
  }

  def concatenate[A](as: List[A], m: Monoid[A]) =
    as.foldLeft(m.zero)(m.op)

  def foldMap[A, B](as: List[A], m: Monoid[B])(f: A => B): B =
    concatenate(as.map(f), m)

  def foldRightOverFoldMap[A, B](as: List[A])(z: B)(f: (A, B) => B): B =
    foldMap(as, endoMonoid[B])(f.curried)(z)

}
