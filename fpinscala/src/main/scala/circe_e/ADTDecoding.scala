package circe_e

import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.generic.semiauto._
import io.circe.parser._
import io.circe.syntax._

object ADTDecoding extends App {

  case class MayClass(name: String)

  sealed trait Event
  case class Event1(myClass: MayClass, l: List[String]) extends Event
  case class Event2(value: Int) extends Event

  implicit val genDevConfig: Configuration =
    Configuration.default.withDiscriminator("$type")

  implicit val decoderEvent: Decoder[Event] = deriveDecoder[Event]
  implicit val encoderEvent: Encoder[Event] = {
    implicit val genDevConfig: Configuration = Configuration.default.withDiscriminator("$type")
    deriveEncoder[Event]
  }

  implicit val decoderMayClass: Decoder[MayClass] = deriveDecoder[MayClass]
  implicit val encoderMayClass: Encoder[MayClass] = deriveEncoder[MayClass]

  val event1: Event = Event1(MayClass("Hey"), List("1"))
  val event1Json: String = event1.asJson.noSpaces
  // {"Event1":{"myClass":{"name":"Hey"},"l":["1"]}}

  println(event1Json)

  val jsonStr = """{"Event1":{"myClass":{"name":"Hey"},"l":["1"]}}"""

  // Right(Event1(MayClass(Hey),List(1)))
  val event = decode[Event](jsonStr)

  println(event)

}
