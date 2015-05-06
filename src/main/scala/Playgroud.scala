object Playgroud extends App {

  def swap(arr: Array[Any]) = {
    arr match {
      case Array(x, y, _*) => arr(0) = y; arr(1) = x; arr
      case _ => arr
    }
  }

  println(Array(-3, 9, 57, 5, 34, 54, 5, 9).toList)
  println(swap(Array(-3, 9, 57, 5, 34, 54, 5, 9)).toList)

}