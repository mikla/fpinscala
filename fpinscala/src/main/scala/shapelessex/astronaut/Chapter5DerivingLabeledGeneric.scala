package shapelessex.astronaut

import shapeless.labelled.FieldType
import shapeless.{::, HList, HNil, LabelledGeneric, Lazy, Witness}

sealed trait JsonValue
case class JsonObject(fields: List[(String, JsonValue)]) extends JsonValue
case class JsonArray(items: List[JsonValue]) extends JsonValue
case class JsonString(value: String) extends JsonValue
case class JsonNumber(Value: Double) extends JsonValue
case class JsonBoolean(value: Boolean) extends JsonValue
case object JsonNull extends JsonValue

trait JsonEncoder[A] {
  def encode(value: A): JsonValue
}

object JsonEncoder {
  def apply[A](implicit enc: JsonEncoder[A]): JsonEncoder[A] = enc

  def instance[A](func: A => JsonValue): JsonEncoder[A] =
    new JsonEncoder[A] {
      override def encode(value: A): JsonValue = func(value)
    }

  implicit val stringEncoder: JsonEncoder[String] = instance(JsonString)
  implicit val doubleEncoder: JsonEncoder[Double] = instance(JsonNumber)
  implicit val intEncoder: JsonEncoder[Int] = instance(d => JsonNumber(d))
  implicit val booleanEncoder: JsonEncoder[Boolean] = instance(JsonBoolean)

  implicit def listEncoder[A](
    implicit enc: JsonEncoder[A]
  ): JsonEncoder[List[A]] = instance(list => JsonArray(list.map(enc.encode)))

  implicit def optionEncoder[A](
    implicit enc: JsonEncoder[A]
  ): JsonEncoder[Option[A]] = instance(opt => opt.map(enc.encode).getOrElse(JsonNull))

}

trait JsonObjectEncoder[A] extends JsonEncoder[A] {
  def encode(value: A): JsonObject
}

object JsonObjectEncoder {
  def instance[A](fn: A => JsonObject): JsonObjectEncoder[A] =
    new JsonObjectEncoder[A] {
      override def encode(value: A): JsonObject = fn(value)
    }

  implicit val hnilObjectEncoder: JsonObjectEncoder[HNil] = instance(_ => JsonObject(Nil))

  implicit def hlistObjectEncoder[K <: Symbol, H, T <: HList](
    implicit
    witness: Witness.Aux[K],
    hEncoder: Lazy[JsonEncoder[H]],
    tEncoder: JsonObjectEncoder[T]
  ): JsonObjectEncoder[FieldType[K, H] :: T] = {
    val fieldName = witness.value.name
    instance { hlist =>
      val head = hEncoder.value.encode(hlist.head)
      val tail = tEncoder.encode(hlist.tail)
      JsonObject((fieldName, head) :: tail.fields)
    }
  }

  implicit def genericObjectEncoder[A, H <: HList](
    implicit
    generic: LabelledGeneric.Aux[A, H],
    hEncoder: Lazy[JsonObjectEncoder[H]]
  ): JsonEncoder[A] = instance(value => hEncoder.value.encode(generic.to(value)))

}

object Chapter5DerivingLabeledGeneric extends App {
  import JsonObjectEncoder._

  println(JsonEncoder[model.Employee].encode(model.Employee("A", 1, true)))

}
