package exception

import org.scalatest.FlatSpec

class OptionSpec extends FlatSpec {
  "Option.orElse" should "have size 0" in {
    val op = Some(1)
    assert(op.orElse(Some(4)) == Some(1))
    assert(None.orElse(Some(4)) == Some(4))
    assert(None.flatMap(r => Some(4)) == None)
    assert(Some(4).filter(x => x == 5) == None)
    assert(None.filter(x => x == 4) == None)
  }

  "variance test" should "return Double" in {
    Ch3Exercises.variance(Seq(1, 2, 3, 4))
  }

}
