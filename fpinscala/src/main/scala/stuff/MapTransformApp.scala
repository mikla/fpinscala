package stuff

import model.Employee

object MapTransformApp extends App {

  val employees = List(
    Employee("A", 1, true),
    Employee("B", 1, true),
    Employee("C", 2, true),
    Employee("Art", 2, true),
    Employee("Bart", 2, true),
    Employee("Cart", 1, true),
  )

  val mapped = employees.groupBy(_.number)

  println(mapped)

  val mappedValues = mapped.mapValues(_.sortBy(_.name))

  println(mappedValues)

}
