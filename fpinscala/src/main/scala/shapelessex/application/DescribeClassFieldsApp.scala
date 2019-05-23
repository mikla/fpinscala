package shapelessex.application

import shapeless._
import record._
import syntax.singleton._
import record._
import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist.{Mapper, ToTraversable}
import shapeless.ops.record.Keys
import syntax.singleton._
import shapeless.tag.Tagged
import shapelessex.application.Attributes.symbolName
import shapelessex.application.Attributes.symbolName.at
import shapeless._
import record._
import syntax.singleton._
import record._
import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist.{Mapper, ToTraversable}
import shapeless.ops.record.Keys
import syntax.singleton._

object DescribeClassFieldsApp extends App {

  case class Settings(
    locationZone: String,
    openCutOff: Int,
    openCutOff1: Int,
    openCutOff2: Int,
    maxValue: Long
  )

  object symbolName extends Poly1 {
    implicit def atTaggedSymbol[T] = at[Symbol with Tagged[T]](_.name)
  }

  def attrs[T, Repr <: HList, KeysRepr <: HList, MapperRepr <: HList, LUB](obj: T)(
    implicit gen: LabelledGeneric.Aux[T, Repr],
    keys: Keys.Aux[Repr, KeysRepr],
    mapper: Mapper.Aux[symbolName.type, KeysRepr, MapperRepr],
    traversable: ToTraversable.Aux[MapperRepr, List, String],
    traversable1: ToTraversable.Aux[Repr, List, LUB],
  ): List[(String, LUB)] = keys().map(symbolName).toList.zip(gen.to(obj).toList)

  println(attrs(Settings("Riga", maxValue = 10L, openCutOff = 1, openCutOff1 = 2, openCutOff2 = 4)))

}
