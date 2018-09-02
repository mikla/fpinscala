import PureConfigEnumeratumApp.Greeting.GoodBye
import enumeratum.EnumEntry.{Camelcase, CapitalWords, Snakecase, Uppercase}
import enumeratum.{EnumEntry, _}
import pureconfig.module.enumeratum._
import com.typesafe.config.ConfigFactory.parseString
import pureconfig.loadConfig

object PureConfigEnumeratumApp extends App {

  sealed trait Greeting extends EnumEntry

  object Greeting extends Enum[Greeting] {
    val values = findValues
    case object Hello extends Greeting
    case object GoodBye extends Greeting
    case object ShoutGoodBye extends Greeting
  }

  case class Overtimes(daily: Int = 100, weekly: Int = 100)

  case class GreetingConf(start: Greeting, end: Greeting, overtimes: Overtimes = Overtimes())

  val conf = parseString(
    """{
      start: GoodBye
      end: ShoutGoodBye
      overtimes = {
        daily = 10
      }
    }""")

  println(GoodBye.entryName)
  println(loadConfig[GreetingConf](conf))

}
