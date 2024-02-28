package catsex

import cats.syntax.all._

object OptionLetMapApp extends App {

  /** Useful for when you don't want to create an explicit val for a value used more than once
    * in a small anonymous function. Or, when refactoring from Option[A] to just A, but without using cats.Id
    */
  implicit class LetMapper[A](val value: A) extends AnyVal {
    def letMap[T](f: A => T): T = f(value)
  }

  val someValue: Option[Int] = None

  val x = someValue.map(_ / 2).letMap(expr => s"$expr – that's my team number. Team $expr!")

  println(x)

  val opt1: Option[(String, String)] = None
  val opt2: Option[(String, String)] = Some("key2" -> "value2")

  val r = Map("key0" -> "value0") |+| Map.from(opt1) |+| Map.from(opt2)

  println(r)

  println(Map("k0" -> "v0") ++ Map("k1" -> "v1"))

}
