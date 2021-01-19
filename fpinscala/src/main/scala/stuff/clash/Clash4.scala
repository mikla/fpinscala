package stuff.clash

import scala.io.StdIn

/**
  * Created by mikla on 02/08/16.
  */
object Clash4 extends App {
  val n = StdIn.readInt
  var inputs = StdIn.readLine.split(" ")
  val digs = for (i <- 0 until n) yield {
    val digit = inputs(i).toInt
    digit
  }

  digs.sorted.mkString

  // Write an action using println
  // To debug: Console.err.println("Debug messages...")

  println(digs.sorted.reverse.mkString)

}
