package stuff.hackerrank.sorting

import scala.collection.mutable

// Minimum Swaps 2
/**
  * You are given an unordered array consisting of consecutive integers  [1, 2, 3, ..., n]
  * without any duplicates. You are allowed to swap any two elements. You need to find the minimum number
  * of swaps required to sort the array in ascending order.
  */
object MinimumSwaps2App extends App {

  def minimumSwaps(a: Array[Int]): Int = {

    val positions = a.zipWithIndex.sortBy(_._1)
    val visited = Array.fill(a.length)(false)

    var ans = 0

    a.indices.foreach { i =>

      if (!(visited(i) || positions(i)._2 == i)) {
        var cycle_size: Int = 0
        var j = i
        while (!visited(j)) {
          visited(j) = true
          j = positions(j)._2
          cycle_size = cycle_size + 1
        }

        if (cycle_size > 0)
          ans = ans + (cycle_size - 1)

      }

    }

    ans
  }

  println(minimumSwaps(Array(1, 3, 5, 2, 4, 6, 7)))

}
