package fp_in_scala.datastructure.list

import scala.annotation.tailrec

sealed trait List[+A] {
  def removeFirst(): List[A]
  def setHead[B >: A](e: B): List[B]
  def head: A
  def tail: List[A]
}

case object Nil extends List[Nothing] {
  def removeFirst(): Nothing = throw new Exception("Nil.removeFirst")
  def setHead[B >: Nothing](e: B): List[B] = throw new Exception("Nil.setHead")
  override def head: Nothing = throw new Exception("Nil.head")
  def tail: List[Nothing] = throw new Exception("Nil.tail")
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

  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  def drop[B](l: List[B], n: Int): List[B] =
    if (n == 0) l
    else l match {
      case Nil => throw new Exception("Not enough elements")
      case Cons(_, xs) => drop(xs, n - 1)
    }

  def dropWhile[A](l: List[A])(f: A => Boolean): List[A] =
    l match {
      case Nil => Nil
      case Cons(x, xs) => if (f(x)) dropWhile(xs)(f) else l
    }

  /**
    * Adds all the elements of one list to another
    */
  def append[A](l1: List[A], l2: List[A]): List[A] =
    l1 match {
      case Nil => l2
      case Cons(x, xs) => Cons(x, append(xs, l2))
    }

  def appendViaFoldRight[A](l1: List[A], l2: List[A]): List[A] =
    foldRight(l1, l2)((x, y) => Cons(x, y))

  /**
    * returns all of the elements but the last
    */
  def init[A](l: List[A]): List[A] =
    l match {
      case Nil => Nil
      case Cons(_, Nil) => Nil
      case Cons(y, ys) => Cons(y, init(ys))
    }

  def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B =
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def foldLeft[A, B](as: List[A], z: B)(f: (B, A) => B): B = {
    @tailrec
    def foldy(lst: List[A], acc: B): B =
      lst match {
        case Nil => acc
        case Cons(x, xs) => foldy(xs, f(acc, x))
      }
    foldy(as, z)
  }

  def foldLeft2[A, B](as: List[A], z: B)(f: (B, A) => B): B =
    foldRight(List.reverse(as), z)((x, y) => f(y, x))

  // List(1, 2, 3, 4)
  // acc(4, acc(3, acc(2, acc(f(1, _))))).apply(0)

  // (id, 1) =>
  // b => id(f(1, b))
  // (b => acc(f(2, id(f(1, b)))
  // (b => acc(f(3, acc(f(2, id(f(1, b))))
  // (b => acc(f(4, acc(f(3, acc(f(2, id(f(1, b)))))
  /**
    * http://stackoverflow.com/questions/17136794/foldleft-using-foldright-in-scala
    */
  def foldRightViaFoldLeft[A, B](as: List[A], z: B)(f: (A, B) => B): B =
    foldLeft(as, (b: B) => b)((acc: (B => B), a: A) => b => acc(f(a, b)))(z)

  def foldLeftViaFoldRight[A, B](as: List[A], z: B)(f: (B, A) => B): B =
    foldRight(as, (b: B) => b)((a, g) => b => g(f(b, a)))(z)

  def length[A](list: List[A]): Int =
    foldLeft(list, 0)((x, _) => x + 1)

  def reverse[A](list: List[A]): List[A] =
    foldLeft(list, Nil: List[A])((x, y) => Cons(y, x))

  def concat[T](list: List[List[T]]): List[T] =
    foldLeft(list, (b: List[T]) => b)((g, a) => b => g(List.foldRight(a, b)((x, y) => Cons(x, y))))(Nil)

  /**
    * Write a function that transforms a list of integers by adding  1 to  each  element.
    */
  def incEach(list: List[Int]): List[Int] = {
    def inc(x: Int): Int = x + 1
    List.foldRight(list, Nil: List[Int])((x, y) => Cons(inc(x), y))
  }

  /**
    * Write a function that turns each value in a  List[Double] into a String
    */
  def toStringEach(list: List[Double]): List[String] =
    List.foldRight(list, Nil: List[String])((x, y) => Cons(x.toString, y))

  def map[A, B](list: List[A])(f: A => B): List[B] =
    List.foldRight(list, Nil: List[B])((x, y) => Cons(f(x), y))

  def filter[A](l: List[A])(f: A => Boolean): List[A] =
    List.foldRight(l, Nil: List[A])((x, y) => if (f(x)) Cons(x, y) else y)

  def flatMap[A, B](l: List[A])(f: A => List[B]): List[B] =
    List.foldRight(l, Nil: List[B])((x, y) => List.foldRight(f(x), y)(Cons(_, _)))

  def filterViaFlatMap[A](list: List[A])(f: A => Boolean): List[A] =
    List.flatMap(list)(x => if (f(x)) Cons(x, Nil) else Nil)

  def startsWith[A](l: List[A], s: List[A]): Boolean = {
    def stWith[A](list: List[A], sub: List[A]): Boolean =
      list match {
        case Cons(listHead, listTail) =>
          sub match {
            case Cons(subHead, subTail) =>
              if (subHead == listHead) stWith(listTail, subTail)
              else false
            case Nil => true
          }
        case Nil =>
          sub match {
            case Nil => true
            case _ => false
          }
      }
    stWith(l, s)
  }

  /**
    * This is realization with accumulator. The difference with hasSubsequence is hasSubsequenceAcc(List(1, 2), Nil) returns false.
    * Whereas hasSubsequence(List(1, 2), Nil) returns true
    */
  def hasSubsequenceAcc[A](list: List[A], sub: List[A]): Boolean = {
    def sub1(l: List[A], s: List[A], acc: Boolean): Boolean =
      l match {
        case Cons(x, xs) =>
          s match {
            case Cons(_, _) =>
              if (x == s.head) sub1(xs, s.tail, acc = true)
              else if (acc) sub1(l, sub, acc = false)
              else sub1(xs, sub, acc = false)
            case Nil => true
          }
        case Nil =>
          s match {
            case Nil => true
            case _ => false
          }
      }
    sub1(list, sub, acc = false)
  }

  def hasSubsequence[A](list: List[A], sub: List[A]): Boolean =
    if (startsWith(list, sub)) true
    else
      list match {
        case Cons(_, xs) => hasSubsequence(xs, sub)
        case Nil => false
      }

}
