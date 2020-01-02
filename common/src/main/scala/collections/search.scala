package collections

import scala.annotation.tailrec

object search {

  def binarySearchFunctional(list: Array[Int], target: Int): Option[Int] = {
    @tailrec
    def bsf(list: Array[Int], target: Int, start: Int, end: Int): Option[Int] = {
      if (start>end) return None
      val mid = start + (end-start+1)/2
      list match {
        case (arr:Array[Int]) if (arr(mid)==target) => Some(mid)
        case (arr:Array[Int]) if (arr(mid)>target) => bsf(list, target, start, mid-1)
        case (arr:Array[Int]) if (arr(mid)<target) => bsf(list, target, mid+1, end)
      }
    }
    bsf(list, target, 0, list.length-1)
  }

}
