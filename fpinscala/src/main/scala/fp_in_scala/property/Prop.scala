package fp_in_scala.property

import fp_in_scala.property.Prop.{FailedCase, SuccessCount}

trait Prop {

  def check: Either[(FailedCase, SuccessCount), SuccessCount]

  def &&(p: Prop): Prop = ???

}

object Prop {
  type SuccessCount = Int
  type FailedCase = String
}
