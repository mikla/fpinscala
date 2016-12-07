package shapelessex.astronaut

import shapelessex.astronaut.core.{CsvEncoder, Employees}

object Chapter3AutomaticDerivation extends App {

  def writeCsv[A](values: List[A])(implicit enc: CsvEncoder[A]): String =
    values.map(value => enc.encode(value).mkString(",")).mkString("\n")

  println(writeCsv(Employees.employees))

}
