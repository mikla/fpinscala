package application

import com.typesafe.config.ConfigFactory.parseString
import pureconfig.ConfigSource
import pureconfig.generic.semiauto._
import pureconfig._
import pureconfig.configurable._
import pureconfig.ConvertHelpers._

object PureConfigSemiautoApp extends App {

  case class Conf(p: java.time.Duration)

  implicit val confReader = deriveReader[Conf]

  val conf = parseString(
    """{
      p = 10 min
    }""")

  val c = ConfigSource.fromConfig(conf).loadOrThrow[Conf]

  println(c)

}
