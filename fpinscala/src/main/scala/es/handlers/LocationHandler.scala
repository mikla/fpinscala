package es.handlers

import es._
import es.events.{Event, LocationAdded, LocationEvent, LocationNameChanged}
import model.Location

class LocationHandler extends EventHandler[Event, Aggregate] {

  def handle(state: Aggregate): PartialFunction[Event, Aggregate] = {
    case event: LocationEvent => event match {
      case LocationAdded(id, name) =>
        state.copy(locations = state.locations :+ Location(id, name))

      case LocationNameChanged(name) => state
    }
  }

}
