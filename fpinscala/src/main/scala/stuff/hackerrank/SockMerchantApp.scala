package stuff.hackerrank

object SockMerchantApp extends App {

  def sockMerchant(n: Int, ar: Array[Int]): Int =
    ar.toList.groupBy(identity).values.foldLeft(0) { case (acc, l) =>
      acc + (l.length / 2)
    }
  print(sockMerchant(9, Array(10, 20, 20, 10, 10, 30, 50, 10, 20)))
}
