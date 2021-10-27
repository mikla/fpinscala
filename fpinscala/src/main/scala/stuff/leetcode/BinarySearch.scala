package stuff.leetcode

import scala.annotation.tailrec

// 704. Binary Search
// https://leetcode.com/problems/binary-search/
object BinarySearch extends App {

  def search(nums: Array[Int], target: Int): Int = {

    @tailrec
    def search0(n: Array[Int], indexOffset: Int): Int =
      n.length match {
        case 0 => -1
        case 1 =>
          if (n.head == target) indexOffset else -1
        case _ =>
          val median = n.length / 2
          val (leftArr, rightArr) = n.splitAt(median)
          if (n(median - 1) < target) search0(rightArr, indexOffset + leftArr.length)
          else search0(leftArr, indexOffset)
      }

    search0(nums, 0)
  }

  println(search(Array(1, 2, 3, 4, 5, 6, 7, 8, 9), 9))

}
