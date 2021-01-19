package shapelessex.astronaut.core

import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, Lazy}
import shapelessex.astronaut.Chapter3RecursiveTypes.Tree

trait CsvEncoder[A] {
  def encode(value: A): List[String]
}

object CsvEncoder {

  def apply[A](implicit ev: CsvEncoder[A]) = ev

  def instance[A](f: A => List[String]): CsvEncoder[A] =
    new CsvEncoder[A] {
      override def encode(value: A): List[String] = f(value)
    }

  implicit val hnilEncoder: CsvEncoder[HNil] = instance(_ => Nil)

  implicit def hlistEncoder[H, T <: HList](
    implicit
    hEncoder: Lazy[CsvEncoder[H]],
    tEncoder: CsvEncoder[T]): CsvEncoder[H :: T] = instance[H :: T] {
    case h :: t => hEncoder.value.encode(h) ++ tEncoder.encode(t)
  }

  implicit def genericEncoder[A, R](
    implicit
    gen: Generic.Aux[A, R], //  { type Repr = R }
    enc: Lazy[CsvEncoder[R]]): CsvEncoder[A] = instance(a => enc.value.encode(gen.to(a)))

  // now time to define encoders for copropduct. we are too lazy to define them by hand.
  implicit val cnilEncoder: CsvEncoder[CNil] = instance(_ => throw new Exception("smth went wrong"))

  implicit def coproductEncoder[H, T <: Coproduct](
    implicit
    hEncoder: Lazy[CsvEncoder[H]],
    tEncoder: CsvEncoder[T]): CsvEncoder[H :+: T] = instance {
    case Inl(h) => hEncoder.value.encode(h)
    case Inr(t) => tEncoder.encode(t)
  }

}

object defaultEncoders {
  import CsvEncoder._

  implicit val intEncoder = instance[Int](i => List(i.toString))
  implicit val stringEncoder = instance[String](str => List(str))
  implicit val booleanEncoder = instance[Boolean](b => List(b.toString))
  implicit val doubleEncoder = instance[Double](b => List(b.toString))

}
