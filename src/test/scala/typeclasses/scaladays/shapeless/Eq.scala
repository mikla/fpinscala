package typeclasses.scaladays.shapeless

import shapeless.{Lazy, Generic}

trait Eq[T] {
  def eqv(x: T, y: T): Boolean
}

object Eq {
  implicit val eqInt: Eq[Int] = new Eq[Int] {
    override def eqv(x: Int, y: Int): Boolean = x == y
  }

  implicit def eqString: Eq[String] = new Eq[String] {
    override def eqv(x: String, y: String): Boolean = x == y
  }

  /**
   * Generic - map type T to R
   */
  implicit def eqGeneric[T, R](implicit gen: Generic.Aux[T, R], eqRepr: Lazy[Eq[R]]): Eq[T] = new Eq[T] {
    override def eqv(x: T, y: T): Boolean = ???
  }
}


