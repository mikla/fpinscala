package stuff

import java.time.ZonedDateTime

case class Location[T](id: String, data: T)
case class MinuteBased[T](value: T)
case class SnapshotOffer(snapshot: Any)

object PatternMatchingOverType extends App {

  type ZonedLocation = Location[MinuteBased[ZonedDateTime]]

  val StringLocation: ZonedLocation = Location("1", MinuteBased(ZonedDateTime.now()))

  def offer(offer: SnapshotOffer) = offer match {
    case SnapshotOffer(locaton: ZonedLocation) =>
      println(locaton)
  }

  offer(SnapshotOffer(StringLocation))

}
