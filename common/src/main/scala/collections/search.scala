package collections

import scala.annotation.tailrec
import scala.collection.{IndexedSeqLike, SeqLike}
import scala.collection.Searching._
import scala.collection.generic.IsSeqLike

object search {

  def binarySearchFunctional(list: Array[Int], target: Int): Option[Int] = {
    @tailrec
    def bsf(list: Array[Int], target: Int, start: Int, end: Int): Option[Int] = {
      if (start > end) return None
      val mid = start + (end - start + 1) / 2
      list match {
        case (arr: Array[Int]) if (arr(mid) == target) => Some(mid)
        case (arr: Array[Int]) if (arr(mid) > target) => bsf(list, target, start, mid - 1)
        case (arr: Array[Int]) if (arr(mid) < target) => bsf(list, target, mid + 1, end)
      }
    }

    bsf(list, target, 0, list.length - 1)
  }

  class BinarySearchImpl[A, Repr](override val coll: SeqLike[A, Repr]) extends SearchImpl[A, Repr](coll) {
    def binarySearch(e: A)(implicit o: Ordering[A]): Option[A] = {
      search(e)(o) match {
        case Found(_) => Some(e)
        case _ => None
      }
    }
  }

  implicit def binarySearch[Repr, A](coll: Repr)
    (implicit fr: IsSeqLike[Repr]): BinarySearchImpl[fr.A, Repr] = new BinarySearchImpl(fr.conversion(coll))

}
