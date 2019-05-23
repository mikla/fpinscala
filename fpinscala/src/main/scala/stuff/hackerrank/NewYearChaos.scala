package stuff.hackerrank

object NewYearChaos extends App {

  def minimumBribes(q: Array[Int]) {
    var ans = 0
    var i = q.length - 1
    while (i >=0) {
      if (q(i) - (i + 1) > 2) {
        println("Too chaotic")
        return 0
      }
      var j = Math.max(0, q(i) - 2)
      while (j < i) {
        if (q(j) > q(i)) ans = ans + 1
        j = j + 1
      }
      i = i - 1
    }
    println(ans)
  }

  minimumBribes(Array(2, 1, 5, 3, 4))


}
