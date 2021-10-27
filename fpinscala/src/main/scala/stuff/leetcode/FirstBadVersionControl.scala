package stuff.leetcode

import scala.annotation.tailrec

trait VersionControl {
  def isBadVersion(version: Int): Boolean = version match {
    case 1 => false
    case 2 => false
    case 3 => false
    case 4 => true
    case 5 => true
    case 6 => false
    case 7 => true
    case 8 => true
    case 9 => false
  }
}

object Solution extends App with VersionControl {

  // [1, 2, 3, 4, ... n]
  // https://leetcode.com/problems/first-bad-version/
  def firstBadVersion(n: Int): Int = {

    @tailrec
    def dichotomy(low: Int, high: Int): Int =
      if (high - low <= 1) {
        if (isBadVersion(low)) low else high
      } else {
        val median = low + (high - low) / 2
        if (isBadVersion(median)) dichotomy(low, median)
        else dichotomy(median, high)
      }

    dichotomy(1, n)
  }

  println(firstBadVersion(9))
}
