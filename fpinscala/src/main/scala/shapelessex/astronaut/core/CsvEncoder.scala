package shapelessex.astronaut.core

import shapeless.{::, Generic, HList, HNil}

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
    hEncoder: CsvEncoder[H],
    tEncoder: CsvEncoder[T]
  ): CsvEncoder[H :: T] = instance[H :: T] {
    case h :: t => hEncoder.encode(h) ++ tEncoder.encode(t)
  }

  implicit def genericEncoder[A, R](
    implicit
    gen: Generic.Aux[A, R], //  { type Repr = R }
    enc: CsvEncoder[R]
  ): CsvEncoder[A] = instance(a => enc.encode(gen.to(a)))

}

object defaultEncoders {
  import CsvEncoder._

  implicit val intEncoder = instance[Int](i => List(i.toString))
  implicit val stringEncoder = instance[String](str => List(str))
  implicit val booleanEncoder = instance[Boolean](b => List(b.toString))

}
