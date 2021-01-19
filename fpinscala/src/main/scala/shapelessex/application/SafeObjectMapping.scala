package shapelessex.application

import shapeless._
import syntax.typeable._

object SafeObjectMapping extends App {

  def receive(tpe: String, obj: Any) = {}

  val l: Any = List(Vector("foo", "bar", "baz"), Vector("wibble"))
  l.cast[List[Vector[String]]]

  receive("string", "a")

}
