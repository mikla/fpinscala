package shapelessex.astronaut
import shapeless._

case class Employee(name: String, number: Int, manager: Boolean)
case class IceCream(name: String, numCherries: Int, inCone: Boolean)

object Chapter1GenericApp extends App {

  def employeeCsv(e: Employee): List[String] =
    List(e.name, e.number.toString, e.manager.toString)

  def iceCreamCsv(c: IceCream): List[String] =
    List(c.name, c.numCherries.toString, c.inCone.toString)

  val genericEmployee = Generic[Employee].to(Employee("Name", 1, true))

  val genericIceCream = Generic[IceCream].to(IceCream("Name", 2, false))

  def genecricCsv(gen: String :: Int :: Boolean :: HNil): List[String] =
    List(gen(0), gen(1).toString, gen(2).toString)

  println(genecricCsv(genericEmployee))
  println(genecricCsv(genericIceCream))

}
