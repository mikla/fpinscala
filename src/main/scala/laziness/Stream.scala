package laziness

sealed trait Stream[+A] {
  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, t) => Some(h())
  }

  def headOptionViaFoldRight: Option[A] =
    this.foldRight(None: Option[A])((a, b) => Some(a))

  def tail: Stream[A] = this match {
    case Empty => throw new Exception("Stream.tail is empty")
    case Cons(h, t) => t()
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
    loop(() => this, 0, Empty).reverse
  }

  def drop(n: Int): Stream[A] = {
    def loop(stream: Stream[A], iter: Int, acc: Stream[A]): Stream[A] = {
      if (iter < n) loop(stream.tail, iter + 1, acc)
      else stream match {
        case Empty => acc
        case Cons(h, t) => loop(stream.tail, iter + 1, Cons(h, () => acc))
      }
    }
    loop(this, 0, Empty).reverse
  }

  def reverse: Stream[A] = {
    def loop(stream: () => Stream[A], acc: Stream[A]): Stream[A] = {
      stream() match {
        case Empty => acc
        case Cons(h, t) => loop(t, Cons(h, () => acc))
      }
    }
    loop(() => this, Empty)
  }

  def takeWhile(p: A => Boolean): Stream[A] = {
    def loop(stream: Stream[A], acc: Stream[A]): Stream[A] = {
      stream.headOption.map { h =>
        if (p(h)) loop(stream.tail, Cons(() => h, () => acc))
        else acc
      } getOrElse Empty
    }
    loop(this, Empty)
  }

  def takeWhileViaFoldRight(p: A => Boolean): Stream[A] = {
    this.foldRight(Empty: Stream[A])((a, b) => if (p(a)) Cons(() => a, () => b) else b)
  }

  def exists(p: A => Boolean): Boolean = {
    def loop(stream: Stream[A]): Boolean = {
      stream match {
        case Empty => false
        case Cons(h, t) => p(h()) || loop(t())
      }
    }
    loop(this)
  }

  def exists2(p: A => Boolean): Boolean = this match {
    case Cons(h, t) => p(h()) || t().exists2(p)
    case _ => false
  }

  def exists3(p: A => Boolean): Boolean =
    foldRight(false)((a, b) => p(a) || b)

  def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
    case Cons(h, t) => f(h(), t().foldRight(z)(f))
    case _ => z
  }

  def forAll(p: A => Boolean): Boolean = this match {
    case Cons(h, t) => p(h()) && t().forAll(p)
    case _ => true
  }

  def map[B >: A](f: A => B): Stream[B] =
    this.foldRight(Empty: Stream[B])((a, b) => Cons(() => f(a), () => b))

  def filter(p: A => Boolean): Stream[A] =
    this.foldRight(Empty: Stream[A])((a, b) => if (p(a)) Stream.cons(a, b) else b)

  def flatMap[B >: A](f: A => Stream[B]): Stream[B] =
    this.foldRight(Empty: Stream[B])((a, b) => f(a).foldRight(b)((x, y) => Stream.cons(x, y)))

  def append[B >: A](s2: Stream[B]): Stream[B] = this match {
    case Empty => s2
    case Cons(h, t) => Cons(h, () => t().append(s2))
  }

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

  def constant[A](a: A): Stream[A] = {
    val constantStream = Stream.cons(a, constantStream)
    constantStream
  }

  def from(n: Int): Stream[Int] = ???

}

