package shapelessex.astronaut.core

trait CsvEncoder[A] {
  def encode(value: A): List[String]
}

object CsvEncoder {

  def apply[A](implicit ev: CsvEncoder[A]) = ev

  def instance[A](f: A => List[String]): CsvEncoder[A] =
    new CsvEncoder[A] {
      override def encode(value: A): List[String] = f(value)
    }


  implicit val employeeEncoder: CsvEncoder[Employee] =
    new CsvEncoder[Employee] {
      def encode(e: Employee): List[String] =
        List(
          e.name,
          e.number.toString,
          if (e.manager) "yes" else "no"
        )
    }

}
