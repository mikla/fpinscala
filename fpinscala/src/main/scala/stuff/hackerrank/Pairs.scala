package stuff.hackerrank

import scala.annotation.tailrec
import scala.io.Source

object Solution {

  def main(args: Array[String]) {

//    val nk = readLine
//    val (n, k) = (nk.split(" ")(0).toLong, nk.split(" ")(1).toLong)

    val lines = Source.fromFile("/Users/mikla/projects/fpinscala/pairs.txt").getLines().toIndexedSeq

    val nk = lines(0)
    val (n, k) = (nk.split(" ")(0).toLong, nk.split(" ")(1).toLong)

    val arr = lines(1).split(" ").map(_.toInt).sorted

    @tailrec
    def loop(left: Int, right: Int, acc: Int): Int = {
      if (right >= n) acc
      else {
        val diff = arr(right) - arr(left)
        diff - k match {
          case 0 => loop(left, right + 1, acc + 1)
          case e if e < 0 => loop(left, right + 1, acc)
          case e if e > 0 => loop(left + 1, right, acc)
        }
      }
    }

    println {
      loop(0, 1, 0)
    }
  }
}
