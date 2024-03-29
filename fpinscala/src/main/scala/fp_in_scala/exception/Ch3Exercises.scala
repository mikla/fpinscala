package fp_in_scala.exception

object Ch3Exercises {

  def variance(xs: Seq[Double]): OptionType[Double] =
    if (xs.length > 0) {
      val avg = xs.sum / xs.length
      SomeType(xs.foldLeft(0.0)((z, x) => z + Math.pow(x - avg, 2)) / xs.length)
    } else NoneType

  def insuranceRateQuote(age: Int, numberOfSpeedTickets: Int): Double = ???
  def parseInsuranceRateQuote(age: String, numberOfSpeedTickets: String): Option[Double] = {
    val optAge: Option[Int] = Try(age.toInt)
    val optSpeedTickets: Option[Int] = Try(numberOfSpeedTickets.toInt)
    map2(optAge, optSpeedTickets)(insuranceRateQuote)
  }

  def Try[A](a: => A): Option[A] =
    try Some(a)
    catch { case _: Exception => None }

  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
    a.flatMap(aa => b.map(bb => f(aa, bb)))

  /**
    * EXERCISE 4
    */
  def sequence[A](a: List[Option[A]]): Option[List[A]] =
    a.foldRight(Some(Nil): Option[List[A]])((x, acc) => map2(x, acc)(_ :: _))

  /**
    * EXERCISE 4.5
    */
  def traverse[A, B](list: List[A])(f: A => Option[B]): Option[List[B]] = {
    def loop(acc: Option[List[B]], rest: List[A]): Option[List[B]] =
      if (rest.isEmpty) acc
      else loop(map2(f(rest.head), acc)(_ :: _), rest.tail)

    loop(Some(Nil): Option[List[B]], list).map(_.reverse)
  }

  def traverse2[A, B](list: List[A])(f: A => Option[B]): Option[List[B]] =
    list.foldLeft((b: Option[List[B]]) => b)((g, a) => b => g(map2(f(a), b)(_ :: _)))(Some(Nil: List[B]))

  /**
    * EXERCISE 4.7
    * Implement sequence and traverse for Either. These should return the  first  error that’s encountered, if there is one.
    */
  def map2[E, A, B, C](a: Either[E, A], b: Either[E, B])(f: (A, B) => C): Either[E, C] =
    a match {
      case Left(v) => Left(v)
      case Right(v) => b match {
          case Left(v1) => Left(v1)
          case Right(v1) => Right(f(v, v1))
        }
    }

  def sequence[E, A](es: List[Either[E, A]]): Either[E, List[A]] =
    es.foldRight(Right(Nil): Either[E, List[A]])((x, acc) => map2(x, acc)(_ :: _))

  def traverseEither[E, A, B](es: List[A])(f: (A) => Either[E, B]): Either[E, List[B]] =
    es.foldLeft((b: Either[E, List[B]]) => b)((g, a) => b => g(map2(f(a), b)(_ :: _)))(Right(Nil): Either[E, List[B]])

}
