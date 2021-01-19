package es.events

import java.util.UUID

sealed trait Event

sealed trait EmployeeEvent extends Event

case class EmployeeAdded(name: String, number: Int, isManager: Boolean) extends EmployeeEvent
case class EmployeeNameChanged(name: String) extends EmployeeEvent

sealed trait LocationEvent extends Event

case class LocationAdded(id: UUID, name: String) extends LocationEvent
case class LocationNameChanged(name: String) extends LocationEvent
