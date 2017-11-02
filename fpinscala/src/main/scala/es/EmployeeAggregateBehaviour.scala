package es

import es.commands.{AddEmployeeCommand, AddLocationCommand, EmployeeCommand}
import es.events.{EmployeeAdded, Event, LocationAdded}
import es.handlers.{EmployeeHandler, LocationHandler}

class EmployeeAggregateBehaviour
  extends AggregateRootBehaviour[Aggregate, EmployeeCommand, Event, EmployeeError] {

  override def processInitializationCommand(command: EmployeeCommand) =
    processLifecycleCommand(Aggregate.empty)(command)

  override def applyInitializationEvent(event: Event) =
    applyLifecycleEvent(Aggregate.empty)(event)

  override def processLifecycleCommand(state: Aggregate)(command: EmployeeCommand) = command match {
    case AddEmployeeCommand(employee) =>
      CommandReaction.accept(EmployeeAdded(employee.name, employee.number, employee.manager))
    case AddLocationCommand(location) =>
      CommandReaction.accept(LocationAdded(location.id, location.name))
  }

  val employeeHandler = new EmployeeHandler
  val locationHandler = new LocationHandler

  val allHandlers = List(employeeHandler, locationHandler)

  override def applyLifecycleEvent(state: Aggregate)(event: Event): Aggregate =
    allHandlers
      .find(_.handle(state).isDefinedAt(event))
      .map(_.handle(state)(event))
      .getOrElse(state)

}