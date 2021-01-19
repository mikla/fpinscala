package stuff.hackerrank

object FibonacciWordsApp extends App {

  def finWords(a: String, b: String, n: BigInt): Char = {
    var aa = a
    var bb = b
    var cc = aa + bb
    while (cc.length <= n) {
      aa = bb
      bb = cc
      cc = aa + bb
    }
    println(cc)
    cc.charAt(n.toInt - 1)
  }

  println(finWords("1415926535", "8979323846", 35))
  println(finWords(
    "1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679",
    "8214808651328230664709384460955058223172535940812848111745028410270193852110555964462294895493038196",
    BigInt("104683731294243150")
  ))

  // 14159265358979323846897932384614159265358979323846
  /*
  val n = readInt
  val lines = for(i <- 0 until n) yield {
    val line = readLine
    line
  }
   */

// 0 1 1 2 3 5 8
//   a b c

}
