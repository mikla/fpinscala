package es

import java.util.UUID

import es.events.{EmployeeAdded, LocationAdded}
import es.handlers.{EmployeeHandler, LocationHandler}

object EsApp extends App {

  val aggregate = Aggregate(List.empty, List.empty)

  val aggregateBehavior = new EmployeeAggregateBehaviour

  val employeeHandler = new EmployeeHandler
  val locationHandler = new LocationHandler

  val employeeEvent = EmployeeAdded("n", 1, true)
  val locationEvent = LocationAdded(UUID.randomUUID(), "name")

  val state = aggregateBehavior.applyLifecycleEvent(
    aggregateBehavior.applyLifecycleEvent(aggregate)(employeeEvent))(locationEvent)

  println(state)

}
