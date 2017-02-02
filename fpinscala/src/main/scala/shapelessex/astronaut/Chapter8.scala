package shapelessex.astronaut

import shapeless._
import shapeless.ops.{hlist, coproduct, nat}

object Chapter8 extends App {

  val hlistLength = hlist.Length[Int :: String :: Boolean :: HNil]
  val coproductLength = coproduct.Length[String :+: Int :+: CNil]

  println(Nat.toInt[hlistLength.Out])
  println(Nat.toInt[coproductLength.Out])

}
