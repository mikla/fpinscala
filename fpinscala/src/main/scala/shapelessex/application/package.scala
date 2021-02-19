package shapelessex

package object application {
  case class UserId(id: String) extends AnyVal

  sealed trait Event

  sealed trait PermissionEvent extends Event
  case class PermissionsChanged(name: String, userId: UserId) extends PermissionEvent

  sealed trait LocationEvent extends Event
  case class LocationCreated(name: String) extends LocationEvent

}
