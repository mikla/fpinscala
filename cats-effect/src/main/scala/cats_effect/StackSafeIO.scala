package cats_effect

import cats.effect.IO
import cats.effect.unsafe.implicits.global

object StackSafeIO extends App {

  def deepUnsafe(n: Int): Int =
    if (n > 0) n * deepUnsafe(n - 1)
    else 1

  def deep(n: Int): IO[Int] =
    IO(n > 0).flatMap {
      case true =>
        deep(n - 1)
      case false =>
        IO(1)
    }

//  println(factorialNonSafe(5000))

  println(deep(10000000).unsafeRunSync())

}
