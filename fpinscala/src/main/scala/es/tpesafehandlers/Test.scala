package es.tpesafehandlers

import es.Aggregate
import es.events.LocationAdded
import shapeless.{:+:, CNil}

object Test {

  val locationHandler = new LocationEventHandler
  val employeeHandler = new EmployeeEventHandler

//  val handler: Handler[LocationEventHandler :+: EmployeeEventHandler :+: CNil, Aggregate] =
//    locationHandler :++: employeeHandler :++: CNilHandler()

}
