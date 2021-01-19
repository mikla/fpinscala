package es

sealed trait EmployeeError

case class Err(err: String) extends EmployeeError
