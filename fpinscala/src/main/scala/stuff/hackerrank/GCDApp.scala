package stuff.hackerrank

object GCDApp extends App {

  def gcd(x: Int, y: Int): Int =
    if (x == y) x
    else if (x > y) gcd(x - y, y)
    else gcd(x, y - x)

  println(gcd(4851, 3003))

  def gcdF(x: Int, y: Int): Int =
    if (y != 0) gcdF(y, x % y) else x

  println(gcdF(4851, 3003))

}
