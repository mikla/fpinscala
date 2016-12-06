package stuff

sealed abstract case class Nat private(toInt: Int)

object Nat {
  def fromInt(n: Int): Option[Nat] =
    if (n >= 0) Some(new Nat(n) {}) else None
}


object NatApp extends App {

  // without sealed abstract you can still do .get.copy(-45)
  println(Nat.fromInt(-4))
  println(Nat.fromInt(4))


}
