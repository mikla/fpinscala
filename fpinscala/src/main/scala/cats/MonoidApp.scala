package cats

import cats.implicits._

object MonoidApp extends App {

  // Monoid extends Semigroup and adds empty

  Monoid[String].empty // ""

  println {
    Monoid[Map[String, Int]].combineAll(List(Map("a" -> 1, "b" -> 2), Map("a" -> 3)))
  }

  val l = List(1, 2, 3, 4, 5)

  l.foldMap(identity) // 15
  l.foldMap(_.toString) // 12345

  def monoidTuple[A: Monoid, B: Monoid]: Monoid[(A, B)] = new Monoid[(A, B)] {
    override def empty: (A, B) = (Monoid[A].empty, Monoid[B].empty)

    override def combine(x: (A, B), y: (A, B)): (A, B) = {
      (Monoid[A].combine(x._1, y._1), Monoid[B].combine(x._2, y._2))
    }
  }

  println {
    l.foldMap(v => (v, v.toString))(monoidTuple)
  }

  println {
    Monoid[Int].empty
  }

}
