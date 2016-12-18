package catsex

import cats._
import cats.data.Xor
import cats.implicits._
import print._

object FoldableApp extends App {

  Semigroup.combine(List(1, 2), List(3, 4, 5)).print("Combine: ")

  Foldable[List].foldK(List(List(1, 2), List(3, 4, 5))).print("foldK: ")
  Foldable[List].foldK(List(None, Option("two"), Option("three"))).print("foldK: ")

  Foldable[List].find(List(1, 2, 3))(_ > 2).print("Foldable.find: ")

  Foldable[List].toList(List(1, 2, 3))
  Foldable[Option].toList(Option(42)).print("Foldable.toList: ")


  Foldable[Option].filter_(Some(42))(_ != 42).print("Foldable[Option].filter_: ")

  // A => G[B]
  Foldable[List].traverse_(List("1", "2", "3"))(parseInt).print("Foldable.traverse strange ")

  val foldableListOption = Foldable[List].compose[Option]
  foldableListOption.fold(List(Option(1), Option(2), Option(3), Option(4))).print("Foldable.compose ")
  foldableListOption.fold(List(Option("1"), Option("2"), None, Option("3"))).print("Foldable.compose with None")

  def parseInt(s: String): Option[Int] =
    Xor.catchOnly[NumberFormatException](s.toInt).toOption

}
