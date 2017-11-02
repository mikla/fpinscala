package es.tpesafehandlers

import es.Aggregate
import es.events.EmployeeEvent

class EmployeeEventHandler extends Handler[EmployeeEvent, Aggregate] {

  override def handle(o: EmployeeEvent) = ???

}
