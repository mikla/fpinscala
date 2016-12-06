package stuff.hackerrank

object GCDApp extends App {

  def gcd(x: Int, y: Int): Int = {
    if (x == y) x
    else if (x > y) gcd(x - y, y)
    else gcd(x, y - x)
  }

  println(gcd(22, 131))

}
