package shapelessex.astronaut

import shapeless._
import shapeless.record._

object Chapter6Records extends App {

  val employee = Employee("mikla", 1, false)

  val repr = LabelledGeneric[Employee].to(employee)

  println(repr.get('name))

}
