package state

import org.scalatest.FlatSpec

class StateSpec extends FlatSpec {
  "SimpleRNG nonNegativeInt" should ">0 and < Int.MaxINt" in {
    val value = SimpleRNG.nonNegativeInt(SimpleRNG(10))._1
    assert(value >= 0 && value <= Int.MaxValue)
  }
}


