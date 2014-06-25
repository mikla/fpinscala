package exception

object Ch3Exercises {

  def variance(xs: Seq[Double]): OptionType[Double] = {
    if (xs.length > 0) {
      val avg = xs.sum / xs.length
      SomeType(xs.foldLeft(0.0)((z, x) => z + Math.pow(x - avg, 2)) / xs.length)
    } else NoneType
  }

  def insuranceRateQuote(age: Int, numberOfSpeedTickets: Int): Double = ???
  def parseInsuranceRateQuote(age: String, numberOfSpeedTickets: String): Option[Double] = {
    val optAge: Option[Int] = Try(age.toInt)
    val optSpeedTickets: Option[Int] = Try(numberOfSpeedTickets.toInt)
    map2(optAge, optSpeedTickets)(insuranceRateQuote)
  }

  def Try[A](a: => A): Option[A] =
      try Some(a)
      catch {case e: Exception => None}

  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = {
    a.flatMap(aa => b map (bb => f(aa, bb)))
  }

  /**
   * EXERCISE 4
   */
  def sequence[A](a: List[Option[A]]): Option[List[A]] = {
    a.foldRight(Some(Nil): Option[List[A]])((x, acc) => map2(x, acc)(_ :: _))
  }

}
