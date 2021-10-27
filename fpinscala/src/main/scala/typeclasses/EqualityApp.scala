package typeclasses

import scala.annotation.nowarn

trait Equality[L, R] {
  def equals(left: L, right: R): Boolean
}

object EqualityApp extends App {

  implicit def sameTypeEquality[T] = new Equality[T, T] {
    @nowarn
    override def equals(left: T, right: T): Boolean = left.equals(right)
  }

  def isEqual[L, R](left: L, right: R)(implicit ev: Equality[L, R]): Boolean =
    ev.equals(left, right)

  isEqual(2, 3)

//  val c = Class.forName("java.lang.String").newInstance()
//  "adds".asInstanceOf[c.type]
}
