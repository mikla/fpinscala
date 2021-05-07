package es.handlers

import es._
import es.events.{EmployeeAdded, EmployeeEvent, EmployeeNameChanged, Event}
import model.Employee

class EmployeeHandler extends EventHandler[Event, Aggregate] {

  def handle(state: Aggregate): PartialFunction[Event, Aggregate] = {
    case event: EmployeeEvent => event match {
        case EmployeeAdded(name, number, isManager) =>
          state.copy(employees = state.employees :+ Employee(name, number, isManager))

        case EmployeeNameChanged(_) => state
      }
  }

}
