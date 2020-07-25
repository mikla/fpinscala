package shapelessex

import java.util.UUID

import shapeless.{::, Generic, HNil}

import scala.language.implicitConversions

trait WithConstruct[T] {
  def construct(uuid: UUID)(implicit U: UUIDConstruct[T]): T = UUIDConstruct.construct[T](uuid)
}

case class WorkTypeId(value: UUID) extends AnyVal
object WorkTypeId extends WithConstruct[WorkTypeId]

case class AbsenceTypeId(value: UUID) extends AnyVal

case class EmployeeId(value: UUID) extends AnyVal

case class NotId()

trait UUIDConstruct[T] {
  def construct(uuid: UUID): T
}

object UUIDConstruct {

  def construct[T](uuid: UUID)(implicit U: UUIDConstruct[T]): T = U.construct(uuid)

  implicit def genericUUIDGenerator[T](
    implicit G: Generic.Aux[T, UUID :: HNil]
  ): UUIDConstruct[T] = (uuid: UUID) => G.from(uuid :: HNil)

}

object AutoUUIDGeneratorApp extends App {

  import UUIDConstruct._

  //  WorkTypeId.generate

  println(construct[EmployeeId](UUID.randomUUID()))
  println(implicitly[UUIDConstruct[EmployeeId]].construct(UUID.randomUUID()))

  //  println(EmployeeId.generate)
  //  println(WorkTypeId.generate())
  //  println(AbsenceTypeId.generate())
  //  println(implicitly[UUIDGenerator[NotId]].generate())

}
