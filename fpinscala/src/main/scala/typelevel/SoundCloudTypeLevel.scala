package typelevel

trait Nat
trait Z extends Nat
trait Succ[T <: Nat] extends Nat

trait MinusOne[A <: Nat] {
  type Res <: Nat
}

object MinusOne {

//  type Aux

}


object SoundCloudTypeLevel {

}
