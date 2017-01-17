package stuff

import java.util.UUID

case class EmployeeId(value: UUID) extends AnyVal

case class Employee(id: EmployeeId, name: String)
object Employee {
  val Unknown = Employee(EmployeeId(UUID.randomUUID()), "Oops")

  def resolve(empls: List[Employee])(employeeId: EmployeeId): Employee =
    empls.find(_.id == employeeId).getOrElse(Unknown)
}

case class Work(employeeId: EmployeeId, name: String)

object ByIdResolution extends App {

  val employeeId = EmployeeId(UUID.randomUUID())
  val employee = Employee(employeeId, "Employee Name")
  val allEmployees = List(employee)

  val works = List(Work(employeeId, "Work"), Work(employeeId, "Meeting"))

  works.foreach { work =>
    println(Employee.resolve(allEmployees)(work.employeeId).name + ", " + work.name)
  }


}
