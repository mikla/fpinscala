package stuff.leetcode

import scala.annotation.tailrec

// https://leetcode.com/problems/search-insert-position
object SearchInsertPosition extends App {

  def searchInsert(nums: Array[Int], target: Int): Int = {

    @tailrec
    def search0(n: Array[Int], indexOffset: Int): Int =
      n.length match {
        case 1 =>
          if (n.head == target) indexOffset
          else {
            // just a corner case when nums contains single element.
            if (indexOffset + 1 == nums.length) {
              if (n.head > target) indexOffset else indexOffset + 1
            } else indexOffset
          }

        case _ =>
          val median = n.length / 2
          val (leftArr, rightArr) = n.splitAt(median)
          if (n(median - 1) < target) search0(rightArr, indexOffset + leftArr.length)
          else search0(leftArr, indexOffset)
      }

    search0(nums, 0)
  }

  println(searchInsert(Array(1), 2))

}
