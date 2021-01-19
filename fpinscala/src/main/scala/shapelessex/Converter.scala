package shapelessex

import model.poly.ToChange
import shapeless.ops.hlist.Mapper
import shapeless.{::, Generic, HList, HNil, Poly1}

trait Converter[T] {
  def polyConvert(t: T): T
}

object Converter extends ConvertedImplicits

trait ConvertedImplicits extends LowLowe {

  implicit val toChangedConverter = new Converter[ToChange] {
    override def polyConvert(t: ToChange): ToChange = {
      println("c")
      ToChange("prefixed-" + t.value)
    }
  }

  implicit val hnilGenerator = new Converter[HNil] {
    override def polyConvert(t: HNil): HNil = HNil
  }

  object toChange extends Poly1 {
    implicit def toChangeCase = at[ToChange](_ => ToChange("changed"))
  }

  implicit def hlistProtocol[H, T <: HList](
    implicit
    PH: Converter[H],
    PT: Converter[T]
  ) = new Converter[H :: T] {
    override def polyConvert(t: H :: T): H :: T = PH.polyConvert(t.head) :: PT.polyConvert(t.tail)
  }

  implicit def productWithIdHead[T, TT, Repr <: HList](
    implicit
    G1: Generic.Aux[T, Repr],
    //    M: Mapper.Aux[toChange.type, Repr, Repr],
    C: Converter[Repr]
  ): Converter[T] = (t: T) => G1.from(C.polyConvert(G1.to(t)))

}

trait LowLowe {
  implicit def unchagedTypesConverted[A] = new Converter[A] {
    override def polyConvert(t: A): A = t
  }
}
