package monix

import monix.eval.Coeval

import scala.annotation.tailrec

object CoevalApp extends App {

  @tailrec
  def fib(cycles: BigInt, a: BigInt, b: BigInt): BigInt =
    if (cycles > 0)
      fib(cycles - 1, b, a + b)
    else
      b

  def fibCoeval(cycles: BigInt, a: BigInt, b: BigInt): Coeval[BigInt] =
    Coeval.eval(cycles > 0).flatMap {
      case true =>
        fibCoeval(cycles - 1, b, a + b)
      case false =>
        Coeval.now(b)
    }

}
