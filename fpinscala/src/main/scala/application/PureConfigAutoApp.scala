package application

import pureconfig.ConfigSource
import pureconfig.generic.auto._

object PureConfigAutoApp extends App {

  case class Conf(contactPoints: List[String])

  val conf = ConfigSource.default.load[Conf]

  println(conf)

}
