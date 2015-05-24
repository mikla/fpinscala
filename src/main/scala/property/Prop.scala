package property

import property.Prop.{SuccessCount, FailedCase}

trait Prop { self  =>
  def && (p: Prop): Prop /*= new Prop {
    override def check = Prop.this.check && p.check
  }*/
  def check: Either[(FailedCase, SuccessCount), SuccessCount]
}

object Prop {
  type FailedCase = String
  type SuccessCount = Int
}
