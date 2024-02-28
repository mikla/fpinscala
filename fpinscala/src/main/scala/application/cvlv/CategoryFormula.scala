package application.cvlv

import scala.io.Source

object CategoryFormula extends App {

  val formulaTemplate =
    """REPLACE(c3167202d02e00ee3166a8930031ec90@/vacancies/categories[2];, 15, "SALES")""".stripMargin

  val source =
    Source.fromFile("/Users/user/projects/personal/fpinscala/fpinscala/src/main/scala/application/cvlv/categories.csv")

  val categories = source
    .getLines()
    .map(_.replace("\"", ""))
    .map(_.split(",") match {
      case Array(id, city) => (id, city)
    }).toList

  val category = categories.foldLeft(formulaTemplate) {
    case (formula, (cityId, name)) =>
      s"""REPLACE($formula,"$cityId","$name")""".stripMargin
  }

  println(category)

}
