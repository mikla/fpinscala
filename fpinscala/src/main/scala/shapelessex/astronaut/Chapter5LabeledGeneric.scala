package shapelessex.astronaut

import shapeless.{HNil, Witness}
import shapeless.labelled.{FieldType, KeyTag}
import shapeless.labelled.field
import shapeless.syntax.singleton._

object Chapter5LabeledGeneric extends App {

  // literal types

  val x42 = 42.narrow // type of x42 is Int(42).
  println(x42)

  // phantom types

  val number = 42
  trait Cherries
  val numCherries = number.asInstanceOf[Int with Cherries] // We end up with a value that is both
  // an Int and a Cherries at compile time, and an Int at runtime:

  val number2 = 43
  val taggedNumber = "number223" ->> number

  field[Cherries](123) // FieldType[Cherries,Int] = 123

  def getFieldName[K, V](
    value: FieldType[K, V]
  )(implicit witness: Witness.Aux[K]): K = witness.value

  println(getFieldName(taggedNumber)) // only for taggged

  def getFieldValue[K, V](value: FieldType[K, V]): V =
    value

  println(getFieldValue(taggedNumber))

  //records

  // read type is
  // FieldType["cat", String] :: FieldType["orange", Boolean] :: HNil
  val garfield = ("cat" ->> "Garfield") :: ("orange" ->> true) :: HNil
}
