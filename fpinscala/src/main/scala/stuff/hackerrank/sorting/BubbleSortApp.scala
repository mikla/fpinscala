package stuff.hackerrank.sorting

// bubble sort
object BubbleSortApp extends App {

  def countSwaps(a: Array[Int]) {
    val len = a.length
    var swaps = 0
    for {
      i <- 0 until len
      j <- 0 until (len - 1)
    } {
      if (a(j) > a(j+1)) {
        val tmp = a(j)
        a(j) = a(j + 1)
        a(j + 1) = tmp
        swaps = swaps + 1
      }
    }

    println(s"Array is sorted in $swaps swaps.")
    println(s"First Element: ${a(0)}")
    println(s"Last Element: ${a(a.length - 1)}")
  }


  countSwaps(Array(1, 3, 0, 9, 2))


}
