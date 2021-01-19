package shapelessex.application

import shapeless._
import shapeless.poly._
import shapeless.record._
import shapeless.ops.record._
import shapeless.ops.hlist.{Mapper, ToTraversable}
import shapeless.tag._

final case class Message(id: Int, title: String, body: String)

trait ToAttributes[T] {
  def toAttributes(v: T): Seq[String]
}

object Attributes {

  object symbolName extends Poly1 {
    implicit def atTaggedSymbol[T] = at[Symbol with Tagged[T]](_.name)
  }

  implicit def familyFormat[T, Repr <: HList, KeysRepr <: HList, MapperRepr <: HList](
    implicit
    gen: LabelledGeneric.Aux[T, Repr],
    keys: Keys.Aux[Repr, KeysRepr],
    mapper: Mapper.Aux[symbolName.type, KeysRepr, MapperRepr],
    traversable: ToTraversable.Aux[MapperRepr, List, String]): ToAttributes[T] =
    new ToAttributes[T] {
      def toAttributes(v: T): Seq[String] = keys().map(symbolName).toList.toSeq
    }

  def toAttributes[T](v: T)(implicit c: ToAttributes[T]): Seq[String] = c.toAttributes(v)

}

object CaseClassAttributesApp extends App {
  import Attributes._
  val message = Message(10, "foo", "bar")
  val attributes = toAttributes(message)

  println(attributes)
  val L = LabelledGeneric[Message].to(message)

  attributes.foreach(attr => println(L('id)))

}
