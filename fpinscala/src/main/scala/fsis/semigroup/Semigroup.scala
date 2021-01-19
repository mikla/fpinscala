package fsis.semigroup

import simulacrum.{op, typeclass}

@typeclass trait Semigroup[A] {
  @op("|+|") def combine(a: A, b: A): A
}

trait SemigroupLaws[A] {
  implicit val simigroupA: Semigroup[A]
  import Semigroup.ops._

  def semigroupAssociativity(a: A, b: A, z: A) =
    (a |+| (b |+| z)) == ((a |+| b) |+| z)

}

object Semigroup {
  def semigroupList[A] = new Semigroup[List[A]] {
    override def combine(a: List[A], b: List[A]): List[A] = a ++ b
  }
}
