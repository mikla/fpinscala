package es.handlers

trait EventHandler[Event, State] {
  def handle(event: State): PartialFunction[Event, State]
}
