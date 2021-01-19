package stuff.clash

import scala.io.StdIn

/**
  * Created by mikla on 02/08/16.
  */
class Clash3 {
  val m = StdIn.readInt
  val n = StdIn.readInt
  var inputs = StdIn.readLine.split(" ")
  val list = for (i <- 0 until n) yield {
    val e = inputs(i).toInt
    e
  }

  val mods = list.foldLeft(0)((acc, elem) => acc + elem % n)

  println(mods)

}
