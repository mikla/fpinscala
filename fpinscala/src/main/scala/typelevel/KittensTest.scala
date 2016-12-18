package typelevel

import cats.Functor
import cats.derived._
import cats.derived.functor._
import cats.derived.functor.legacy._
import cats.syntax.functor._

case class ArmImpl[T](t: T)

case class DomainObject[T](
  first: T,
  second: T,
  non: ArmImpl[T],
  all: List[T])

object KittensTest extends App {

  implicit val domainFunctor = Functor[DomainObject]

  val stringDomain = DomainObject("first", "second", new ArmImpl("str"), List("a", "b"))

  domainFunctor.map(stringDomain)(_ + "_") // DomainObject(first_,second_,Arm(str_,+),List(a_, b_))
  domainFunctor.map(stringDomain)(a => List(a)) // DomainObject(List(first),List(second),List(List(a), List(b)))

}
