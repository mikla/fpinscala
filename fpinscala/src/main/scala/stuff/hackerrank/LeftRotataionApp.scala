package stuff.hackerrank

object LeftRotataionApp extends App {

  // Complete the rotLeft function below.
  def rotLeft(a: Array[Int], d: Int): Array[Int] = {
    a.drop(d) ++ a.take(d)
  }


  println(rotLeft(Array(1, 2, 3, 4, 5), 2).mkString(" "))


}
