package shapelessex.astronaut

import shapeless._
import shapeless.ops.hlist.{IsHCons, Last}

trait Second[T <: HList] {
  type Out
  def apply(value: T): Out
}

object Second {
  type Aux[L <: HList, O] = Second[L] { type Out = O }
  def apply[L <: HList](implicit inst: Second[L]): Aux[L, inst.Out] = inst

  implicit def hlistSecond[A, B, Rest <: HList]: Aux[A :: B :: Rest, B] = new Second[A :: B :: Rest] {
    override type Out = B
    override def apply(value: A :: B :: Rest): B = value.tail.head
  }
}

object Chapter4DependentTypes extends App {
  val second1 = Second[String :: Boolean :: HList]
  println(second1("foot" :: false :: HNil))

  def lastField[A, Repr <: HList](input: A)(
    implicit
    gen: Generic.Aux[A, Repr],
    last: Last[Repr]
  ): last.Out = last.apply(gen.to(input))

  // we are trying to get value from case class
  def genSingleParameter[A, H](
    value: A
  )(
    implicit gen: Generic.Aux[A, H :: HNil]
  ): H = gen.to(value).head

  // genSingleParameter(Circle(1.0)) compiles until we call it.

  def getWrappedValue[A, Repr <: HList, Head, Tail <: HList](
    input: A
  )(
    implicit
    gen: Generic.Aux[A, Repr],
    ev: IsHCons.Aux[Repr, Head, Tail]
  ): Head = gen.to(input).head

  println(getWrappedValue(Circle(1.0)))

}
