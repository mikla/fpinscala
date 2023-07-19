package fsis.monoid

import fsis.semigroup.Semigroup

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

trait MonoidLaws {

  // left identity
  // right identity

}
