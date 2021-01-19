package model

case class Employee(name: String, number: Int, manager: Boolean)

object Employees {
  val employees: List[Employee] = List(
    Employee("Bill", 1, true),
    Employee("Peter", 2, false),
    Employee("Milton", 3, false)
  )

}
