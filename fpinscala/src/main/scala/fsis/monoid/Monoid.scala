package fsis.monoid

import fsis.semigroup.Semigroup
import simulacrum.typeclass

@typeclass trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

trait MonoidLaws {

  // left identity
  // right identity

}
