package stuff.hackerrank

// Plus Minus
object PlusMinus extends App {

  def plusMinus(arr: Array[Int]) {
    val len: Double = arr.length.toDouble
    val pos = arr.count(_ > 0) / len
    val neg = arr.count(_ < 0) / len
    val zeros = arr.count(_ == 0) / len
    println(f"$pos%1.6f")
    println(f"$neg%1.6f")
    println(f"$zeros%1.6f")
  }

  plusMinus(Array(-4, 3, -9, 0, 4, 1))

}
