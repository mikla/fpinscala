package shapelessex

import shapeless.PolyDefns.~>
import shapeless.poly._
import cats.print._
import shapeless.Poly1

object PolyApp extends App {

  object choose extends (Set ~> Option) {
    def apply[T](s: Set[T]) = s.headOption
  }


  choose(Set(1, 2, 3)).print("choose 1")
  choose(Set('a', 'b', 'c')).print("choose 2")

  object size extends Poly1 {
    implicit def caseInt = at[Int](x ⇒ 1)
    implicit def caseString = at[String](_.length)
    implicit def caseTuple[T, U](implicit st: Case.Aux[T, Int], su: Case.Aux[U, Int]) =
      at[(T, U)](t ⇒ size(t._1) + size(t._2))
  }

  // in Scala int's possible to define function
  def listToSet(l: List[Int]): Set[Int] = ???
  val listToSetVal = listToSet _

  // but type Int is restricted here.
  // what if we want to have something like this:

  def listToSetT[T](l: List[T]): Set[T] = ???
  val listToSetTV = listToSetT _ // List[Nothing] => Set[Nothing] What?!

  // here shapeless comes...

  val polyOptionToList = new (Option ~> List) {
    override def apply[T](f: Option[T]): List[T] = f.toList
  }

  val res1 = polyOptionToList(Option(1))


}
