package exception

import org.scalatest.FlatSpec

class EitherSpec extends FlatSpec {

  "Either map2" should "Right(12)" in {
    assert(Ch3Exercises.map2(Right(10), Right(2))((x, y) => Right(x + y)) == Right(12))
  }

  "Either map2" should "Left(err2)" in {
    val l2: Either[String, Int] = Left("err2")
    assert(Ch3Exercises.map2(Right(10), l2)((x, y) => Right(x + y)) == Left("err2"))
  }

  "Either map2" should "Left(err1)" in {
    val l1: Either[String, Int] = Left("err1")
    assert(Ch3Exercises.map2(l1, Right(2))((x, y) => Right(x + y)) == Left("err1"))
  }

}
