package exception

object Ch3Exercises {

  def variance(xs: Seq[Double]): Option[Double] = {
    if (xs.length > 0) {
      val avg: Double = xs.sum / xs.length
      println(avg)
    }
    Some(2)
  }

}
