package property

import property.Prop.{SuccessCount, FailedCase}

trait Prop {
  def && (p: Prop): Prop
  def check: Either[(FailedCase, SuccessCount), SuccessCount]
}

object Prop {
  type FailedCase = String
  type SuccessCount = Int
}
