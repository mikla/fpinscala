package shapelessex.astronaut

import shapeless.ops.hlist
import shapeless.{::, HList, HNil, _}

// return second last element
trait Penultimate[L] {
  type Out
  def apply(l: L): Out
}

object Penultimate {
  type Aux[L, O] = Penultimate[L] { type Out = O }

  def apply[L](implicit p: Penultimate[L]): Aux[L, p.Out] = p

  implicit def hlistPenultimate[L <: HList, M <: HList, O](
    implicit
    init: hlist.Init.Aux[L, M],
    last: hlist.Last.Aux[M, O]): Penultimate.Aux[L, O] = new Penultimate[L] {
    type Out = O

    def apply(t: L): O = last.apply(init.apply(t))
  }

  implicit def genericPenultimate[CaseClass, Repr, Elem](
    implicit
    gen: Generic.Aux[CaseClass, Repr],
    p: Penultimate.Aux[Repr, Elem]): Penultimate.Aux[CaseClass, Elem] = new Penultimate[CaseClass] {
    override type Out = Elem
    override def apply(l: CaseClass): Elem = p.apply(gen.to(l))
  }

}

object Chapter6HListAndCoproducts extends App {

  import Penultimate._

  type BigList = String :: Int :: Boolean :: Double :: HNil
  val bigList: BigList = "foo" :: 1 :: true :: 1.0 :: HNil

  println(Penultimate[BigList].apply(bigList))
  println(Penultimate[model.Employee].apply(model.Employee("Mikla", 1, false)))

}
