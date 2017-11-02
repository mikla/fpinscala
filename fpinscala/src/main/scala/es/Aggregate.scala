package es

import model.{Employee, Location}

case class Aggregate(employees: List[Employee], locations: List[Location])

object Aggregate {

  def empty = Aggregate(List.empty, List.empty)

}
