package circe_e

import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

object ADTDecoding extends App {

  case class MayClass(name: String)

  sealed trait Event
  case class Event1(myClass: MayClass, l: List[String]) extends Event
  case class Event2(value: Int) extends Event

  //  implicit val decoderEvent: Decoder[Event] = deriveDecoder[Event]
  //  implicit val encoderEvent: Encoder[Event] = deriveEncoder[Event]

  val event1: Event = Event1(MayClass("Hey"), List("1"))
  val event1Json: String = event1.asJson.noSpaces

  println(event1Json)

  val jsonStr = """{"Event1":{"name":"Hey","l":["1"]}}"""

  val event = decode[Event](jsonStr)

  println(event)

  sealed trait TaskItemStatus
  object TaskItemStatus {
    final case object Pending extends TaskItemStatus
    final case object Running extends TaskItemStatus
    final case object Improving extends TaskItemStatus
    final case object Stopped extends TaskItemStatus
  }

  @JsonCodec case class Item(
    id: String,
    status: TaskItemStatus
  )

  println(Item("id", TaskItemStatus.Stopped))

}
