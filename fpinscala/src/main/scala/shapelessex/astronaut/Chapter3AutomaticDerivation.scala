package shapelessex.astronaut

import shapeless._
import shapelessex.astronaut.core.CsvEncoder
import shapelessex.astronaut.core.CsvEncoder._
import shapelessex.astronaut.core.defaultEncoders._
import io.print._
import model.Employees

object Chapter3AutomaticDerivation extends App {

  def writeCsv[A](values: List[A])(implicit enc: CsvEncoder[A]): String =
    values.map(value => enc.encode(value).mkString(",")).mkString("\n")

  writeCsv(List.empty[String :: Int :: Boolean :: HNil])

  // first approach
  val gen = Generic[model.Employee]
  val reprEncoder: CsvEncoder[gen.Repr] = implicitly

  reprEncoder.encode(gen.to(Employees.employees.head)).print
  val employeeAutoEncoder = implicitly[CsvEncoder[model.Employee]]

  writeCsv(AllShapes).print // automatic derivation of CsvEncoder[Shape]

}
