package stuff.clash

import scala.io.StdIn

object Clash extends App {

  var s: scala.collection.mutable.Set[Char] = scala.collection.mutable.Set.empty[Char]

  val n = StdIn.readInt()

  val sets = for (i <- 0 until n) yield {
    val w = StdIn.readLine
    w.foldLeft(Set.empty[Char]) { (acc: Set[Char], elem: Char) =>
      if (elem.isLetter) acc + elem
      else acc
    }
  }

  val x = sets.reduce(_ ++ _).toSeq.mkString
  println(x)

}
