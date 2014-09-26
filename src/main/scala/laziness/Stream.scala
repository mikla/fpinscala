package laziness

import scala.concurrent.Future

sealed trait Stream[+A] {
  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, t) => Some(h())
  }

  def toList: List[A] = {
    def loop(stream: () => Stream[A], acc: List[A]): List[A] = {
      stream() match {
        case Empty => acc
        case Cons(h, t) => loop(t, h() :: acc)
      }
    }
    loop(() => this, List.empty).reverse
  }

  def take(n: Int): Stream[A] = {
    def loop(stream: () => Stream[A], iter: Int, acc: Stream[A]): Stream[A] = {
      if (iter < n)
        stream() match {
          case Empty => throw new Exception("not enough elements in stream")
          case Cons(h, t) => loop(t, iter + 1, Cons(h, () => acc))
        }
      else acc
    }
    loop(() => this, 0, Empty)
  }

  def drop(n: Int): Stream[A] = ???
}

case object Empty extends Stream[Nothing]

case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))

}

