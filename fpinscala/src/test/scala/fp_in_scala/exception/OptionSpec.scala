package fp_in_scala.exception

import org.scalatest.flatspec.AnyFlatSpec

class OptionSpec extends AnyFlatSpec {
  "Option.orElse" should "have size 0" in {
    val op = SomeType(1)
    assert(op.orElse(SomeType(4)) == SomeType(1))
    assert(NoneType.orElse(SomeType(4)) == SomeType(4))
    assert(NoneType.flatMap(r => SomeType(4)) == NoneType)
    assert(SomeType(4).filter(x => x == 5) == NoneType)
    assert(NoneType.filter(x => x == 4) == NoneType)
  }

  "variance test" should "return Double" in {
    assert(Ch3Exercises.variance(Seq(1, 2, 3, 4)) == SomeType(1.25))
  }

  "sequence test 1" should "return List" in {
    assert(Ch3Exercises.sequence(List(Some(1), Some(2), Some(3), Some(4))) == Some(List(1, 2, 3, 4)))
  }

  "sequence test 2" should "return None" in {
    assert(Ch3Exercises.sequence(List(Some(1), Some(2), Some(3), None)) == None)
  }

}
