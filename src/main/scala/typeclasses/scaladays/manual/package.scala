package typeclasses.scaladays

package object manual {
  implicit class EqOps[T](x: T)(implicit eqT: Eq[T]) {
    def === (y: T): Boolean = eqT.eqv(x, y)
  }
}
