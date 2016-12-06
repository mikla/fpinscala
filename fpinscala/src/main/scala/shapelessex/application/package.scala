package shapelessex

package object application {
  case class UserId(id: String) extends AnyVal

  sealed trait Event
  case class PermissionsChanged(name: String, userId: UserId) extends Event
  case class LocationCreated(name: String) extends Event

}
