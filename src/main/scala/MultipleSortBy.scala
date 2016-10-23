
object MultipleSortBy extends App {

  case class Row(name: String, value: Int)


  val x = List(Row("a", 1), Row("b", 1), Row("b", 2), Row("a", 2), Row("a", 3))

  println {
    x.sortBy(x => (x.value, x.name))
  }

}
