package catsex.effect

import cats.effect.IO

object ReferentialTransparencyApp extends App {

  def f(x: Any, y: Any) = {}

  f(println("1"), println("1"))

  val x = println(2)
  f(x, x)

  def putStr(line: String): IO[Unit] =
    IO {
      println(line)
    }

  f(putStr("h"), putStr("h"))

  val x2 = putStr("h2")

  f(x2, x2)

}
