package fsis.foldable

import fsis.monoid.Monoid

trait Foldable[F[_]] {

  def foldLeft[A, B](fa: F[A], initial: B)(f: (B, A) => B): B

  def foldRight[A, B](fa: F[A], initial: B)(f: (A, B) => B): B

  def foldMap[A, B](fa: F[A])(f: A => B)(implicit M: Monoid[B]): B =
    foldLeft(fa, M.empty)((b, a) => M.combine(b, f(a)))

}
