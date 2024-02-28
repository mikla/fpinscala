package application.cvlv

import scala.io.Source

object TownFormula extends App {

  val formulaTemplate =
    """REPLACE(c3167202d02e00ee3166a8930031ec90@/vacancies/townId;, "543", "Riga")""".stripMargin

  val source =
    Source.fromFile("/Users/user/projects/personal/fpinscala/fpinscala/src/main/scala/application/cvlv/towns.csv")

  val cities = source
    .getLines()
    .map(_.replace("\"", ""))
    .map(_.split(",") match {
      case Array(id, city) => (id, city)
    }).toList

  val townFormula = cities.foldLeft(formulaTemplate) {
    case (formula, (cityId, name)) =>
      s"""REPLACE($formula,"$cityId","$name")""".stripMargin
  }

  println(townFormula)

}
