package typeclasses.scaladays.shapeless

trait Eq[T] {
  def eqv(x: T, y: T): Boolean
}

object Eq {
  implicit val eqInt: Eq[Int] = (x: Int, y: Int) => x == y

  implicit def eqString: Eq[String] = new Eq[String] {
    override def eqv(x: String, y: String): Boolean = x == y
  }

}


