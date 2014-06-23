package exception

object Ch3Exercises {

  def variance(xs: Seq[Double]): OptionType[Double] = {
    if (xs.length > 0) {
      val avg = xs.sum / xs.length
      SomeType(xs.foldLeft(0.0)((z, x) => z + Math.pow(x - avg, 2)) / xs.length)
    } else NoneType
  }
}
