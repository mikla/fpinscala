package stuff

object CountCharge extends App {

  def getChange(purchasePrice: Int = 3, paid: Int = 50): List[(Int, Int)] = {
    val denominations = List(500, 200, 100, 50, 20, 10, 5, 2, 1)

    def loop(denom: List[Int], amount: Int, acc: List[Int] = List()): List[Int] = denom match {
      case coin :: _ =>
        val charge = amount - coin
        loop(denom.filter(_ <= charge), charge, coin :: acc)
      case Nil => acc
    }

    val totalChange = paid - purchasePrice
    loop(denominations.filter(_ <= totalChange), totalChange).groupBy(identity).map {
      case (k, v) => (k, v.size)
    }.toList
  }

  println {
    getChange()
  }

}
