package stuff

object Fump extends App {

  val m: Map[String, Int] = Map("Stri" -> 1, "sdsdf" -> 3)

  m.map(filter)

  def filter(f: (String, Int)): (String, Int) = f

}
