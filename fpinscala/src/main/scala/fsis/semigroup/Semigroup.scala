package fsis.semigroup

trait Semigroup[A] {
  def combine(a: A, b: A): A
}

trait SemigroupLaws[A] {
  implicit val S: Semigroup[A]

  def semigroupAssociativity(a: A, b: A, z: A) =
    S.combine(a, S.combine(b, z)) == S.combine(S.combine(a, b), z)

}

object Semigroup {
  def semigroupList[A] = new Semigroup[List[A]] {
    override def combine(a: List[A], b: List[A]): List[A] = a ++ b
  }
}
