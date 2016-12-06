package stuff.clash

object Clash extends App {

  var s: scala.collection.mutable.Set[Char] = scala.collection.mutable.Set.empty[Char]

  val n = readInt()

  val sets = for(i <- 0 until n) yield {
    val w = readLine
    w.foldLeft(Set.empty[Char]) { (acc: Set[Char], elem: Char) =>
      if (elem.isLetter) acc + elem
      else acc
    }
  }

  val x = sets.reduce(_ ++ _).toSeq.mkString
  println(x)

}
