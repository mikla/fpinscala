package shapelessex.astronaut

import shapeless._
import shapeless.record._

object Chapter6Records extends App {

  val employee = model.Employee("mikla", 1, false)

  val repr = LabelledGeneric[model.Employee].to(employee)

  println(repr.get('name))

}
