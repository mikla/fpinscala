package es.commands

import model.{Employee, Location}

sealed trait EmployeeCommand

case class AddEmployeeCommand(employee: Employee) extends EmployeeCommand

case class AddLocationCommand(location: Location) extends EmployeeCommand