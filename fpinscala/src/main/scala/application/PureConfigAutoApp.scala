package application

import com.typesafe.config.ConfigFactory.parseString
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import pureconfig._
import pureconfig.configurable._
import pureconfig.ConvertHelpers._

object PureConfigAutoApp extends App {

  case class Conf(p: java.time.Duration)

  val conf = parseString(
    """{
      p = 30d
    }""")

  val c = ConfigSource.fromConfig(conf).loadOrThrow[Conf]

  println(c)

}
