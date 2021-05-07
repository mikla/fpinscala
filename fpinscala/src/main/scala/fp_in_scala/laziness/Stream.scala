package fp_in_scala.laziness

sealed trait Stream[+A] {
  def headOptionViaFoldRight: Option[A] =
    this.foldRight(None: Option[A])((a, b) => Some(a))

  def toList: List[A] = {
    def loop(stream: () => Stream[A], acc: List[A]): List[A] =
      stream() match {
        case Empty => acc
        case Cons(h, t) => loop(t, h() :: acc)
      }
    loop(() => this, List.empty).reverse
  }

  def take(n: Int): Stream[A] = {
    def loop(stream: () => Stream[A], iter: Int, acc: Stream[A]): Stream[A] =
      if (iter < n)
        stream() match {
          case Empty => throw new Exception("not enough elements in stream")
          case Cons(h, t) => loop(t, iter + 1, Cons(h, () => acc))
        }
      else acc
    loop(() => this, 0, Empty).reverse
  }

  def takeViaUnfold(n: Int): Stream[A] = Stream.unfold((this, 0)) {
    case (Cons(h, t), taken) if taken < n => Some(h(), (t(), taken + 1))
    case _ => None
  }

  def reverse: Stream[A] = {
    def loop(stream: () => Stream[A], acc: Stream[A]): Stream[A] =
      stream() match {
        case Empty => acc
        case Cons(h, t) => loop(t, Cons(h, () => acc))
      }
    loop(() => this, Empty)
  }

  def drop(n: Int): Stream[A] = {
    def loop(stream: Stream[A], iter: Int, acc: Stream[A]): Stream[A] =
      if (iter < n) loop(stream.tail, iter + 1, acc)
      else stream match {
        case Empty => acc
        case Cons(h, _) => loop(stream.tail, iter + 1, Cons(h, () => acc))
      }
    loop(this, 0, Empty).reverse
  }

  def tail: Stream[A] = this match {
    case Empty => throw new Exception("Stream.tail is empty")
    case Cons(_, t) => t()
  }

  def takeWhile(p: A => Boolean): Stream[A] = {
    def loop(stream: Stream[A], acc: Stream[A]): Stream[A] =
      stream.headOption.map { h =>
        if (p(h)) loop(stream.tail, Cons(() => h, () => acc))
        else acc
      }.getOrElse(Empty)
    loop(this, Empty)
  }

  def takeWhileViaUnfold(p: A => Boolean): Stream[A] = Stream.unfold(this) {
    case Cons(h, t) if p(h()) => Some(h(), t())
    case _ => None
  }

  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, _) => Some(h())
  }

  def takeWhileViaFoldRight(p: A => Boolean): Stream[A] =
    this.foldRight(Empty: Stream[A])((a, b) => if (p(a)) Cons(() => a, () => b) else b)

  def exists(p: A => Boolean): Boolean = {
    def loop(stream: Stream[A]): Boolean =
      stream match {
        case Empty => false
        case Cons(h, t) => p(h()) || loop(t())
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

  def mapViaUnfold[B >: A](f: A => B): Stream[B] =
    Stream.unfold(this) {
      case Cons(h, t) => Some(f(h()), t())
      case _ => None
    }

  def filter(p: A => Boolean): Stream[A] =
    this.foldRight(Empty: Stream[A])((a, b) => if (p(a)) Stream.cons(a, b) else b)

  def flatMap[B >: A](f: A => Stream[B]): Stream[B] =
    this.foldRight(Empty: Stream[B])((a, b) => f(a).foldRight(b)((x, y) => Stream.cons(x, y)))

  def append[B >: A](s2: Stream[B]): Stream[B] = this match {
    case Empty => s2
    case Cons(h, t) => Cons(h, () => t().append(s2))
  }

  def zipWith[B, C](s2: Stream[B])(f: (A, B) => C): Stream[C] = Stream.unfold((this, s2)) {
    case (Cons(h, t), Cons(hh, tt)) => Some(f(h(), hh()), (t(), tt()))
    case _ => None
  }

  /**
    *  def zipAll[B](s: Stream[B]): Stream[(Option[A], Option[B])] = Stream.unfold((this, s)) {
    *    case (Cons(h, t), Cons(hh, tt)) => Some((Some(h()) -> Some(hh()), t() -> tt()))
    *    case (Empty, Cons(hh, tt)) => Some(Option.empty[A] -> Some(hh()), Stream.empty[A] -> tt())
    *    case (Cons(h, t), Empty) => Some(Some(h()) -> Option.empty[B], t() -> Stream.empty[B])
    *    case (Empty, Empty) => None
    *  }
    */

  def zipAll[B](s2: Stream[B]): Stream[(Option[A], Option[B])] =
    zipWithAll(s2)((_, _))

  def zipWithAll[B, C](s2: Stream[B])(f: (Option[A], Option[B]) => C): Stream[C] =
    Stream.unfold((this, s2)) {
      case (Cons(h1, t1), Cons(h2, t2)) => Some(f(Some(h1()), Some(h2())) -> (t1() -> t2()))
      case (Cons(h, t), Empty) => Some(f(Some(h()), Option.empty[B]) -> (t(), Stream.empty[B]))
      case (Empty, Cons(h, t)) => Some(f(Option.empty[A], Some(h())) -> (Stream.empty[A] -> t()))
      case _ => None
    }

  def startsWith[B >: A](s: Stream[B]): Boolean =
    (this, s) match {
      case (Cons(h, t), Cons(hh, tt)) if h() == hh() => t().startsWith(tt())
      case (Cons(_, _), Empty) => true
      case _ => false
    }

  def tails: Stream[Stream[A]] = Stream.unfold(this) {
    case Empty => None
    case s => Some((s, s.drop(1)))
  }.append(Stream(Empty))

  def hasSubsequence[B >: A](s: Stream[B]): Boolean =
    this.tails.exists(_.startsWith(s))

  def scanRight[B](z: B)(f: (A, => B) => B): Stream[B] = this.foldRight(z -> Stream(z)) { (a, b) =>
    val b2 = f(a, b._1)
    (b2, Stream.cons(b2, b._2))
  }._2

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

  def constant[A](a: A): Stream[A] = Stream.cons(a, constant(a))

  def from(n: Int): Stream[Int] = Stream.cons(n, from(n + 1))

  def fibs: Stream[Int] = {
    def loop(x: Int, y: Int): Stream[Int] = Stream.cons(x + y, loop(y, x + y))
    Stream(0, 1).append(loop(0, 1))
  }

  def fibsViaUnfold: Stream[Int] =
    Stream(0, 1).append(unfold((0, 1)) { s =>
      lazy val nFib = s._1 + s._2
      Some(nFib, (s._2, nFib))
    })

  def fromViaUnfold(n: Int): Stream[Int] = unfold(n)(s => Some(s, s + 1))

  def constantViaUnfold[A](a: A): Stream[A] = unfold(a)(s => Some(s, s))

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = f(z) match {
    case Some((elem, next)) => Stream.cons(elem, unfold(next)(f))
    case _ => Empty
  }

}
