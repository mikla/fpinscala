package es.tpesafehandlers


object Test {

  val locationHandler = new LocationEventHandler
  val employeeHandler = new EmployeeEventHandler

//  val handler: Handler[LocationEventHandler :+: EmployeeEventHandler :+: CNil, Aggregate] =
//    locationHandler :++: employeeHandler :++: CNilHandler()

}
