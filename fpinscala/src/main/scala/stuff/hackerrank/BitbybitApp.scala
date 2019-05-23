package stuff.hackerrank

import scala.io.Source

object BitbybitApp {

  def printArr(t: Array[Char]) = {
    t.reverse.foreach(print(_))
    println()
  }


  def main(args: Array[String]): Unit = {
    val filename = "/Users/mikla/projects/fpinscala/fpinscala/src/main/scala/stuff/hackerrank/input.txt"
    val a = Array.fill(32)('?')
    var b = a.clone()

    for (line <- Source.fromFile(filename).getLines.drop(1)) {
      val words: Array[String] = line.split(" ")
      if (words.size == 1) {
        printArr(b)
        b = a.clone()
      } else if (line.contains("CLEAR")) {
        b(words(1).toInt) = '0'
      } else if (words.contains("SET"))
        b(words(1).toInt) = '1'
      else if (words.contains("OR")) {
        if (b(words(1).toInt) == '1' || b(words(2).toInt) == '1')
          b(words(1).toInt) = '1'
        else if (b(words(1).toInt) == '0' || b(words(2).toInt) == '0')
          b(words(1).toInt) = '0'
        else b(words(1).toInt) = '?'
      }
      else if (words.contains("AND")) {
        if (b(words(2).toInt) == '1' && b(words(1).toInt) == '1')
          b(words(1).toInt) = '1'
        else if (b(words(1).toInt) == '0' && b(words(2).toInt) == '0')
          b(words(1).toInt) = '1'
        else b(words(1).toInt) = '?'
      }
    }
  }

}
