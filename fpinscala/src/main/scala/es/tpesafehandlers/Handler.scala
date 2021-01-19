package es.tpesafehandlers

import shapeless.{:+:, CNil, Coproduct, Inl, Inr}

trait Handler[-M, +R] {
  def handle(o: M): R
}

case class CNilHandler[R]() extends Handler[CNil, R] {
  def handle(o: CNil): R = o.impossible
  def :++:[A](handler: Handler[A, R]): ComposedHandlers[A, CNil, R] = ???
}

// accepts headHandler and tailHandler
case class ComposedHandlers[H, T <: Coproduct, R](
  headHandler: Handler[H, R],
  tailHandler: Handler[T, R])
  extends Handler[H :+: T, R] {

  override def handle(o: :+:[H, T]) = ???

  def :++:[A](handler: Handler[A, R]): ComposedHandlers[A, H :+: T, R] = ???

}

// every proof is a type class

trait SubProduct[Inner, Outer <: Coproduct] {
  def lift(i: Inner): Outer
}

object SubProduct {

  // base case: for any type CNil is sub product
  implicit def baseCase[Outer <: Coproduct]: SubProduct[CNil, Outer] = new SubProduct[CNil, Outer] {
    override def lift(i: CNil) = i.impossible
  }

  /*
  implicit def inductiveCase[IH, IT <: Coproduct, Outer <: Coproduct](
    // requirements
    implicit TailIsSubCoproduct: SubProduct[IT, Outer],
    HeadISSubCoproduct: Included[IT, Outer]
  ) */

}

trait Included[A, C <: Coproduct] {
  def include(a: A): C
}

object Included {

  implicit def includedCase[A, C <: Coproduct]: Included[A, A :+: C] = new Included[A, A :+: C] {
    override def include(a: A) = Inl(a)
  }

  implicit def includedCase2[B, A, C <: Coproduct](
    implicit I: A Included C): Included[A, B :+: C] = new Included[A, B :+: C] {
    override def include(a: A) = Inr(I.include(a))
  }

}

// unified handler
