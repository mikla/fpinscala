package catsex

import cats.implicits._
import cats.instances._

object OptionLetMapApp extends App {

  /** Useful for when you don't want to create an explicit val for a value used more than once
    * in a small anonymous function. Or, when refactoring from Option[A] to just A, but without using cats.Id */
  implicit class LetMapper[A](val value: A) extends AnyVal {
    def letMap[T](f: A => T): T = f(value)
  }

  val someValue: Option[Int] = None

  val x = someValue.map(_ / 2).letMap { expr => s"$expr – that's my team number. Team $expr!" }

  println(x)


}
