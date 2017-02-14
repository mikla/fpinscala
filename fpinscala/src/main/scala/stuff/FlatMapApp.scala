package stuff

object FlatMapApp extends App {

  val range = (1 until 5).flatMap(i => None)

  println(range)
  println(range.headOption)

}
