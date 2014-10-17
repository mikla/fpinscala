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

  "SimpleRNG intDouble/doubleInt/double3" should "randoms " in {
    val intDoubleRes = SimpleRNG.intDouble(new SimpleRNG(12))
    val doubleIntRes = SimpleRNG.doubleInt(new SimpleRNG(13))
    val double3Res = SimpleRNG.double3(new SimpleRNG(14))
    println(intDoubleRes)
    println(doubleIntRes)
    println(double3Res)
  }

  "ints" should "List of random Ints" in {
    val rands = SimpleRNG.ints(2)(new SimpleRNG(10))
    assert((rands._1(0) != rands._1(1)) && rands._1.size == 2)
  }

}


