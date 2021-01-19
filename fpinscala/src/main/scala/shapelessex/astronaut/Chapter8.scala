package shapelessex.astronaut

import shapeless._
import shapeless.ops.{coproduct, hlist, nat}

trait SizeOf[A] {
  def value: Int
}

object Chapter8 extends App {

  val hlistLength = hlist.Length[Int :: String :: Boolean :: HNil]
  val coproductLength = coproduct.Length[String :+: Int :+: CNil]

  println(Nat.toInt[hlistLength.Out])
  println(Nat.toInt[coproductLength.Out])

  def sizeOf[A](implicit S: SizeOf[A]): Int = S.value

  implicit def genericSizeOf[A, R <: HList, N <: Nat](
    implicit
    gen: Generic.Aux[A, R],
    len: hlist.Length.Aux[R, N],
    natToInt: nat.ToInt[N]): SizeOf[A] = new SizeOf[A] {
    override def value: Int = natToInt.apply()
  }

  println(sizeOf[Employee])

}
