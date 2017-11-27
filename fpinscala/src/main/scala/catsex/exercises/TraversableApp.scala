package catsex.exercises

import cats.instances.list._
import cats.instances.option._
import cats.syntax.traverse._

object TraversableApp extends App {

  println(List(1, 2).traverse(v => Option(v))) // Some(List(1, 2))
  println(List(1, 2).traverse(v => if (v == 2) None else Option(v))) // None

}
