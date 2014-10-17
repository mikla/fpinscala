package state

import org.scalatest.FlatSpec

class StateSpec extends FlatSpec {
  "SimpleRNG nonNegativeInt" should ">0 and < Int.MaxINt" in {
    val value = SimpleRNG.nonNegativeInt(SimpleRNG(10))._1
    assert(value >= 0 && value <= Int.MaxValue)
  }

  "SimpleRNG double" should "0 < v < 1" in {
    val value = SimpleRNG.double(SimpleRNG(10))._1
    assert(value >=0 && value < 1)
  }

}


