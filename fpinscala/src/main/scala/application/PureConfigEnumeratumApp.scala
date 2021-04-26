package application

import com.typesafe.config.ConfigFactory.parseString
import enumeratum.{Enum, EnumEntry}
import pureconfig.{ConfigReader, loadConfig}
import supertagged.{@@, TaggedType}
import pureconfig.generic.auto._

object PureConfigEnumeratumApp extends App {

  object Location extends TaggedType[String]
  type Location = Location.Type

//  implicit val taggedReader = ConfigReader[String].map(s => Location @@ s)

  implicit def deriveTaggedReader[T : ConfigReader, Tag <: TaggedType[T]] = ConfigReader[T].map(t => @@[Tag](t))
  implicit val locationReader = deriveTaggedReader[String, TaggedType[String]]

  sealed trait Greeting extends EnumEntry

  object Greeting extends Enum[Greeting] {
    val values = findValues
    case object Hello extends Greeting
    case object GoodBye extends Greeting
    case object ShoutGoodBye extends Greeting
  }

  val EnumMap: Map[Greeting, String] = Map(
    Greeting.Hello -> "h",
    Greeting.GoodBye -> "g",
    Greeting.ShoutGoodBye -> "s"
  )

  case class Overtimes(daily: Int = 100, weekly: Int = 100)

  case class GreetingConf(
    start: Greeting,
    end: Greeting,
    overtimes: Overtimes = Overtimes(),
    enabledLocations: List[Location])

  val conf = parseString(
    """{
      start: GoodBye
      end: ShoutGoodBye
      overtimes = {
        daily = 10
      }
      enabled-locations = ["Riga"]
    }""")

  println(loadConfig[GreetingConf](conf))

}
