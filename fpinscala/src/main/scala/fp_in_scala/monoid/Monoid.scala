package fp_in_scala.monoid

trait Monoid[A] {
  def op(a: A, b: A): A
  def zero: A
}

// laws:
// op(op(a, b), c) == op(a, op(b, c))
// op(zero, a) == op(a, zero)

trait MonoidDefs {

  import MonoidInstances._
  def concatenate[A](as: List[A], m: Monoid[A]) =
    as.foldLeft(m.zero)(m.op)

  def foldMap[A, B](as: List[A], m: Monoid[B])(f: A => B): B =
    as.foldLeft(m.zero)((a, b) => m.op(a, f(b)))

  def foldRightOverFoldMap[A, B](as: List[A])(z: B)(f: (A, B) => B): B =
    foldMap(as, endoMonoid[B])(f.curried)(z)

  def foldLeftOverFoldMap[A, B](as: List[A])(z: B)(f: (B, A) => B): B =
    foldMap(as, flip(endoMonoid[B]))(a => b => f(b, a))(z)

  def foldMapV[A, B](v: IndexedSeq[A], m: Monoid[B])(f: A => B): B = {
    def deep(seq: IndexedSeq[A]): B =
      if (seq.length == 2) {
        m.op(f(seq(0)), f(seq(1)))
      } else if (seq.length == 1) {
        m.op(m.zero, f(seq(0)))
      } else {
        val c = seq.length / 2
        m.op(deep(seq.take(c)), deep(seq.drop(c)))
      }
    deep(v)
  }

}
