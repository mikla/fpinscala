package util

import scala.annotation.tailrec

object computation {

  /**
    * Just repeat computation for given init value given amount if times.
    * Result is List of all intermediate computations.
    * Initial value is excluded from result.
    */
  def repeated[A](init: A, f: A => A, times: Int): List[A] = {
    @tailrec
    def loop(seed: Int, init: A, acc: List[A]): List[A] =
      if (seed < times) {
        val newInit = f(init)
        loop(seed + 1, newInit, newInit :: acc)
      } else acc
    loop(0, init, Nil)
  }

}
