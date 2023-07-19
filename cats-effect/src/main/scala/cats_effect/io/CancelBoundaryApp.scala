package cats_effect.io

import cats.effect.IO
import cats.effect.unsafe.implicits.global

object CancelBoundaryApp extends App {

  def fib(n: Int, a: Long, b: Long): IO[Long] =
    IO.defer {
      if (n <= 0) IO.pure(a)
      else {
        val next = fib(n - 1, b, a + b)

        if (n % 10 == 0) IO.cede *> next
        else next
      }
    }

  val n100 = fib(15, 0, 1)

  n100.onCancel(IO(println("cancelled "))).unsafeRunSync()

  //  println(fib(100, 1, 1).unsafeRunSync())
  //  println(fib(102, 1, 1).unsafeRunSync())

}
