package stuff.clash

import scala.io.StdIn

object Clash2 extends App {

  val n = StdIn.readInt
  val lines = for (i <- 0 until n) yield {
    val line = StdIn.readLine
    line
  }

  val zipped = lines.zipWithIndex.foldLeft((Seq.empty[String], Seq.empty[String])) { (acc, elem) =>
    if (elem._2 % 2 == 0) {
      (acc._1 :+ elem._1, acc._2)
    } else (acc._1, acc._2 :+ elem._1)
  }

  println {
    lines.zipWithIndex.span(_._2 % 2 == 0)
  }

  zipped._1.foreach(println)
  zipped._2.foreach(println)

  // Write an action using println
  // To debug: Console.err.println("Debug messages...")

  println("answer")

}
