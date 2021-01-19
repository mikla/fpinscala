package stuff

import cats.Functor
import cats.implicits._

object PolyConvAllApp extends App {

  case class ToChange(value: String)
  case class Level2(many: List[ToChange], i: Int)
  case class Level1(change: ToChange, s: String, level2: Level2)
  case class Outer(optChange: Option[ToChange], level1: Level1, b: Boolean)

  import shapeless.{::, Generic, HList, HNil}

  trait Converter[T] {
    def polyConvert(t: T): T
  }

  object Converter extends ConvertedImplicits

  trait ConvertedImplicits extends LowLowe {

    implicit def functorConv[F[_] : Functor, A](implicit A: Converter[A]): Converter[F[A]] =
      (t: F[A]) => t.map(v => A.polyConvert(v))

    implicit val toChangedConverter = new Converter[ToChange] {
      override def polyConvert(t: ToChange): ToChange =
        ToChange("prefixed-" + t.value)
    }

    implicit val hnilConverter = new Converter[HNil] {
      override def polyConvert(t: HNil): HNil = HNil
    }

    implicit def hlistConverter[H, T <: HList](
      implicit
      PH: Converter[H],
      PT: Converter[T]) = new Converter[H :: T] {
      override def polyConvert(t: H :: T): H :: T = PH.polyConvert(t.head) :: PT.polyConvert(t.tail)
    }

    implicit def productConverter[T, TT, Repr <: HList](
      implicit
      G1: Generic.Aux[T, Repr],
      //    M: Mapper.Aux[toChange.type, Repr, Repr],
      C: Converter[Repr]): Converter[T] = (t: T) => G1.from(C.polyConvert(G1.to(t)))

  }

  trait LowLowe {
    implicit def unchagedTypesConverted[A] = new Converter[A] {
      override def polyConvert(t: A): A = t
    }
  }

  val origibal = Outer(
    Some(ToChange("original1")),
    Level1(ToChange("original2"), "string", Level2(List(ToChange("inlist1"), ToChange("inlist2")), 0)),
    b = false
  )

  println(implicitly[Converter[Outer]].polyConvert(origibal))

}
