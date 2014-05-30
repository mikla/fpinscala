package datastructure.list

sealed trait List[+A] {
  def removeFirst(): List[A]
  def setHead[B >: A](e: B): List[B]
}

case object Nil extends List[Nothing] {
  def removeFirst(): Nothing = throw new Exception("Nil.removeFirst")
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

  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = {
    l match {
      case Nil => Nil
      case Cons(x, xs) => if (f(x)) dropWhile(xs, f) else l
    }
  }

  /**
    * Adds all the elements of one list to another
    */
  def append[A](l1: List[A], l2: List[A]): List[A] = {
    l1 match {
      case Nil => l2
      case Cons(x, xs) => Cons(x, append(xs, l2))
    }
  }

  /**
   * returns all of the elements but the last
   */
  def init[A](l: List[A]): List[A] = {
    l match {
      case Nil => Nil
      case Cons(y, Nil) => Nil
      case Cons(y, ys) => Cons(y, init(ys))
    }
  }
}
