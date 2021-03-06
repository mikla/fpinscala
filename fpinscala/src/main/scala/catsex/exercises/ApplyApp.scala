package catsex.exercises

import cats.Apply
import cats.implicits._

object ApplyApp extends App {

  // F[A => B]
  // A

  val applyOption = new Apply[Option] {
    override def ap[A, B](ff: Option[A => B])(fa: Option[A]): Option[B] = ???
    override def map[A, B](fa: Option[A])(f: (A) => B): Option[B] = ???
  }

  println {
    (Option(1), Option(2), Option(3)).mapN(_ + _ + _)
  }

  val intToString: Int => String = _.toString
  val double: Int => Int = _ * 2

  println {
    Apply[Option].ap(None)(Some(1))
  }

  println {
    Apply[Option].tuple2(Some(1), Some(2))
  }

  val addArity2 = (a: Int, b: Int, c: Int) => a + b + c

}
