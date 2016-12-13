package shapelessex.astronaut

import shapeless._
import shapelessex.astronaut.core.CsvEncoder._
import shapelessex.astronaut.core.defaultEncoders._
import shapelessex.astronaut.core.{CsvEncoder, defaultEncoders}

object Chapter3AutomaticDerivation extends App {

  def writeCsv[A](values: List[A])(implicit enc: CsvEncoder[A]): String =
    values.map(value => enc.encode(value).mkString(",")).mkString("\n")

  writeCsv(List.empty[String :: Int :: Boolean :: HNil])

  // first approach
  val gen = Generic[Employee]
  val reprEncoder: CsvEncoder[gen.Repr] = implicitly

  println(reprEncoder.encode(gen.to(Employees.employees.head)))


  val employeeAutoEncoder = implicitly[CsvEncoder[Employee]]


}
