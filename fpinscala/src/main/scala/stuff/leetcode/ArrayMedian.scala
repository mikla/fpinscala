package stuff.leetcode

import stuff.leetcode.ArrayMedian.Solution.findMedianSortedArrays

import scala.annotation.tailrec

object ArrayMedian extends App{

  object Solution {
    def findMedianSortedArrays(nums1: Array[Int], nums2: Array[Int]): Double = {

      @tailrec
      def loop(n1: List[Int], n2: List[Int], acc: List[Int]): List[Int] = {
        (n1, n2) match {
          case (n1x :: n1rest, n2x :: _) if (n1x < n2x) =>
            loop(n1rest, n2, n1x :: acc)
          case (n1x :: _, n2x :: n2rest) if (n2x <= n1x) =>
            loop(n1, n2rest, n2x :: acc)
          case (Nil, n2x :: n2rest) =>
            loop(Nil, n2rest, n2x :: acc)
          case (n1x :: n1rest, Nil) =>
            loop(n1rest, Nil, n1x :: acc)
          case _ => acc
        }
      }

      val mergedArray = loop(nums1.toList, nums2.toList, List.empty)

      mergedArray match {
        case x :: Nil => x
        case x :: y :: Nil => (x + y) / 2.toDouble
        case rest =>
          val median = mergedArray.length / 2
          if (mergedArray.length % 2 == 0) {
            (mergedArray(median) + mergedArray(median - 1)) / 2.toDouble
          } else {
            mergedArray(median)
          }
      }
    }
  }

  findMedianSortedArrays(List(0, 0).toArray, List(0, 0).toArray)

}
