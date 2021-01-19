package patterns.auxp

import cats.Monoid

trait Foo[A] {
  type B
  def value: B
}

object Foo {
  type Aux[A0, B0] = Foo[A0] {
    type B = B0
  }
}

object AuxPlayGround extends App {

  implicit def fi = new Foo[Int] {
    override type B = String
    override def value: B = "Foo"
  }

  implicit def fs = new Foo[String] {
    override type B = Boolean
    override def value: B = true
  }

  def foo[T](t: T)(implicit f: Foo[T]): f.B = f.value

//  def fooZero[T](t: T)(implicit f: Foo[T], monoid: Monoid[f.B]) // do not compile!

  def fooAux[T, R](t: T)(implicit f: Foo.Aux[T, R], m: Monoid[R]): R = m.empty

  foo("")

}
