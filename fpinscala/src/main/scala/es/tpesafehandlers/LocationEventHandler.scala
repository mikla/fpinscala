package es.tpesafehandlers

import es.Aggregate
import es.events.LocationEvent

class LocationEventHandler extends Handler[LocationEvent, Aggregate] {
  override def handle(o: LocationEvent) = ???
}
