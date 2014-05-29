package datastructure.list

sealed trait List[+A] {
  def removeFirst(): Unit
  def setHead[B >: A](e: B): List[B]
}

case object Nil extends List[Nothing] {
  def removeFirst(): Unit = throw new Exception("Nil.removeFirst")
  def setHead[B >: Nothing](e: B): List[B] = throw new Exception("Nil.setHead")
}

case class Cons[+A](head: A, tail: List[A]) extends List[A] {
  def removeFirst() = tail
  def setHead[B >: A](e: B): List[B] = Cons(e, tail)
}

object List {
  def sum(ints: List[Int]): Int = ints match {
    case Nil => 0
    case Cons(x, xs) => x + sum(xs)
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x + product(xs)
  }

  def apply[A](as: A*): List[A] = {
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))
  }

  def drop[B](l: List[B], n: Int): List[B] = {
    if (n == 0) l
    else l match {
      case Nil => throw new Exception("Not enough elements")
      case Cons(_, xs) => drop(xs, n - 1)
    }
  }
}
