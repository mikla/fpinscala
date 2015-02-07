package xml

import scalaz.Scalaz._
import scalaz._

object ReadzApp extends App {
  val __ = RootPath

  implicit val stringReadz = Readz { node =>
    if (node.text.length > 0) node.text.successNel[String]
    else "not present".failureNel[String]
  }

  val empl = ((__ \ "name").readz[String] |@|
    (__ \ "homePhone").readz[String]) { Person }

  println(xml.readz)

  case class Person(name: String, workPhone: String)

  private lazy val xml = <xml>
    <name>name</name>
    <workPhone>+375292990304</workPhone>
  </xml>

}
