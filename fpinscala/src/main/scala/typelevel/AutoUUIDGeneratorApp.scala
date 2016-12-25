package typelevel

import java.util.UUID

import shapeless.{::, Generic, HNil}

import scala.language.implicitConversions

case class WorkTypeId(value: UUID) extends AnyVal
case class AbsenceTypeId(value: UUID) extends AnyVal
case class EmployeeId(value: UUID) extends AnyVal
case class NotId()

trait UUIDGenerator[T] {
  def generate: T
}

object UUIDGenerator {

  implicit def generate[T](implicit U: UUIDGenerator[T]): T = U.generate

  implicit def genericUUIDGenerator[T](implicit G: Generic.Aux[T, UUID :: HNil]): UUIDGenerator[T] =
    new UUIDGenerator[T] {
      override def generate: T = G.from(UUID.randomUUID() :: HNil)
    }

}

object AutoUUIDGeneratorApp extends App {

  import UUIDGenerator._

  println(generate[EmployeeId])
  println(implicitly[UUIDGenerator[EmployeeId]].generate)

  //  println(EmployeeId.generate)
  //  println(WorkTypeId.generate())
  //  println(AbsenceTypeId.generate())
  //  println(implicitly[UUIDGenerator[NotId]].generate())

}
