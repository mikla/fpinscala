package fixpoint

import cats.Functor
import cats.implicits._
import fixpoint.AwesomeFixApp.IntListF.{IntCons, IntNil}

// https://japgolly.blogspot.com/2017/11/practical-awesome-recursion-ch-01.html

object AwesomeFixApp extends App {

  /**
    * sealed trait IntList
    * final case class IntCons(head: Int, tail: IntList) extends IntList
    * case object IntNil extends IntList
    */

  sealed trait IntListF[F]

  object IntListF {

    final case class IntCons[F](head: Int, tail: F) extends IntListF[F]

    final case class IntNil[F]() extends IntListF[F]

  }

  // More examples of recursive data structures: Tree, Json.

  // Create Functor

  implicit val functor: Functor[IntListF] = new Functor[IntListF] {
    override def map[A, B](fa: IntListF[A])(f: A => B): IntListF[B] = fa match {
      case IntCons(head, tail) => IntCons(head, f(tail))
      case IntNil() => IntNil()
    }
  }

  case class Fix[F[_]](unfix: F[Fix[F]])

  type IntList = Fix[IntListF]

  object IntList {

    def apply(f: IntListF[IntList]): IntList = Fix(f)

    def nil: IntList = apply(IntNil())

    def cons(head: Int, tail: IntList) = apply(IntCons(head, tail))

    def fromList(list: List[Int]): IntList =
      list.foldRight(nil)(cons)

  }

  // catamorphism

  def cata[F[_] : Functor, A](fAlgebra: F[A] => A)(f: Fix[F]): A =
    fAlgebra(f.unfix.map(cata(fAlgebra)))

  type FAlgebra[F[_], A] = F[A] => A

  val listSum: FAlgebra[IntListF, Int] = {
    case IntListF.IntCons(h, t) => h + t
    case IntListF.IntNil() => 0
  }

  def sumList(list: IntList) =
    cata(listSum)(list)

  println(sumList(IntList.fromList((1 to 100).toList)))

}
