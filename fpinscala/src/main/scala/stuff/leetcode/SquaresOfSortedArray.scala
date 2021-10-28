package stuff.leetcode

object SquaresOfSortedArray extends App {

  def sortedSquares(nums: Array[Int]): Array[Int] = {
    nums.map(a => a * a).sorted
  }

  // https://leetcode.com/problems/rotate-array/
  def rotate(nums: Array[Int], k: Int): Unit = {
    (1 to k).foreach { _ =>
      val last = nums.last
      var i = nums.length - 1
      while (i > 0) {
        nums(i) = nums(i - 1)
        i -= 1
      }
      nums(0) = last
    }
  }

  println(rotate(Array(1,2,3,4,5,6,7), 3))

}
