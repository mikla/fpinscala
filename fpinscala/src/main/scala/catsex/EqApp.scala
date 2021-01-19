package catsex

import cats.Eq
import cats.implicits._
import model.Employee

object EqApp extends App {

  implicit val eqInstance = new Eq[Employee] {
    override def eqv(x: Employee, y: Employee): Boolean =
      x.name === y.name && x.number === y.number && x.manager === y.manager
  }

  val employees: List[Employee] = List(Employee("m", 1, true), Employee("r", 2, true))

  employees.find(_ == "")

}
