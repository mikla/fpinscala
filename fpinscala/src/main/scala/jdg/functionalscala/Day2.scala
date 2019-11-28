package jdg.functionalscala

object Day2 extends App {

  // Day 1 recap

  // How many implementations it may have?
  def f6[A, B](f: (A, B) => B, xs: List[A]): B = ???

  // Q: What is ADT?
  // A: Composition of Product and Sum types

  // Q: Why to use ADT?
  // A: Modeling domain that it impossible to have incorrect state

  // Q: What is function?

  // Express non-determenism in function type.

  // Instead of calling now() inside function, accept current time as a function parameter.

  // [* => *, (* => *) => *] => *
  trait Answer6[A[_], B[_[_]]]

  trait CollectionLike[F[_]] {
    def empty[A]: F[A]

    def cons[A](a: A, as: F[A]): F[A]

    def uncons[A](as: F[A]): Option[(A, F[A])]

    final def singleton[A](a: A): F[A] =
      cons(a, empty[A])

    final def append[A](l: F[A], r: F[A]): F[A] =
      uncons(l) match {
        case Some((l, ls)) => append(ls, cons(l, r))
        case None          => r
      }

    final def filter[A](fa: F[A])(f: A => Boolean): F[A] =
      bind(fa)(a => if (f(a)) singleton(a) else empty[A])

    final def bind[A, B](fa: F[A])(f: A => F[B]): F[B] =
      uncons(fa) match {
        case Some((a, as)) => append(f(a), bind(as)(f))
        case None          => empty[B]
      }

    final def fmap[A, B](fa: F[A])(f: A => B): F[B] = {
      val single: B => F[B] = singleton[B](_)

      bind(fa)(f andThen single)
    }
  }

  val ListCollectionLike: CollectionLike[List] = new CollectionLike[List] {
    override def empty[A]: List[A] = Nil
    override def cons[A](a: A, as: List[A]): List[A] = a :: as
    override def uncons[A](as: List[A]): Option[(A, List[A])] = as match {
      case a :: xs => Some((a, xs))
      case _       => None
    }
  }

  //
  // EXERCISE 8
  //
  // Implement `Sized` for `List`.
  //
  trait Sized[F[_]] {
    // This method will return the number of `A`s inside `fa`.
    def size[A](fa: F[A]): Int
  }

  val ListSized: Sized[List] = new Sized[List] {
    override def size[A](fa: List[A]): Int = fa.size
  }

  def MapSized2[K]: Sized[({type l[X] = Map[K, X]})#l] with Object = new Sized[({type l[X] = Map[K, X]})#l] {
    override def size[A](fa: Map[K, A]): Int = fa.size
  }

}
