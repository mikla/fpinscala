package catsex.free

import java.util.UUID

import cats.data.Coproduct
import cats.free.{Free, Inject}
import cats.{Id, ~>}

import scala.language.higherKinds

case class Employee(id: String, name: String, isActive: Boolean)
case class AccountingRecord(id: String, employeeId: String, hoursWorked: Long)

sealed trait EmployeeService[A]
case class CreateEmployee(employee: Employee) extends EmployeeService[Employee]
case class DeleteEmployee(employeeId: String) extends EmployeeService[Unit]


sealed trait AccountingInfoService[A]
case class GetAccountingInfo(employeeId: String) extends AccountingInfoService[List[AccountingRecord]]
case class SaveAccountingInfo(employeeId: String, hoursWorked: Long) extends AccountingInfoService[AccountingRecord]

/** Defining smart algebra to lift our Algebra to Free */
class EmployeeServiceComponent[F[_]](implicit I: Inject[EmployeeService, F]) {
  def create(employee: Employee): Free[F, Employee] = Free.inject[EmployeeService, F](CreateEmployee(employee))
  def delete(employeeId: String): Free[F, Unit] = Free.inject[EmployeeService, F](DeleteEmployee(employeeId))
}

object EmployeeServiceComponent {
  implicit def employeeService[F[_]](implicit I: Inject[EmployeeService, F]): EmployeeServiceComponent[F] =
    new EmployeeServiceComponent
}

class AccountingInfoServiceComponent[F[_]](implicit I: Inject[AccountingInfoService, F]) {
  def getInfo(employeeId: String): Free[F, List[AccountingRecord]] =
    Free.inject[AccountingInfoService, F](GetAccountingInfo(employeeId))
  def saveInfo(employeeId: String, hoursWorked: Long): Free[F, AccountingRecord] =
    Free.inject[AccountingInfoService, F](SaveAccountingInfo(employeeId, hoursWorked))
}

object AccountingInfoServiceComponent {
  implicit def accountingService[F[_]](implicit I: Inject[AccountingInfoService, F]): AccountingInfoServiceComponent[F] =
    new AccountingInfoServiceComponent[F]
}

/** Define interpreters */
object ConsoleEmployeeServiceInterpreter extends (EmployeeService ~> Id) {
  override def apply[A](fa: EmployeeService[A]): Id[A] = fa match {
    case CreateEmployee(employee) =>
      println(s"CreateEmployee($employee)")
      employee
    case DeleteEmployee(employeeId) =>
      println(s"DeleteEmployee($employeeId)")
      ()
  }
}

object ConsoleAccountingServiceInterpreter extends (AccountingInfoService ~> Id) {
  override def apply[A](fa: AccountingInfoService[A]): Id[A] = fa match {
    case GetAccountingInfo(employeeId) =>
      println(s"GetAccountingInfo($employeeId)")
      List.empty
    case SaveAccountingInfo(employeeId, hoursWorked) =>
      println(s"SaveAccountingInfo($employeeId, $hoursWorked)")
      AccountingRecord(UUID.randomUUID().toString, employeeId, hoursWorked)
  }
}

object DependencyInjectionApp extends App {

  type Application[A] = Coproduct[EmployeeService, AccountingInfoService, A]

  def program(
    implicit E: EmployeeServiceComponent[Application],
    A: AccountingInfoServiceComponent[Application]): Free[Application, Long] = {

    import E._, A._

    for {
      employee <- create(Employee(UUID.randomUUID().toString, "Mikla", isActive = true))
      _ <- saveInfo(employee.id, 5)
      _ <- saveInfo(employee.id, 10)
      hours <- getInfo(employee.id)
    } yield hours.map(_.hoursWorked).sum
  }

  val interpreter: Application ~> Id = ConsoleEmployeeServiceInterpreter or ConsoleAccountingServiceInterpreter

  val evaled: Unit = program.foldMap(interpreter)

}
