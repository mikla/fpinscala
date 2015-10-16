package fp_in_scala.exception

import org.scalatest.FlatSpec

class EitherSpec extends FlatSpec {

  "Either map2" should "Right(12)" in {
    assert(Ch3Exercises.map2(Right(10), Right(2))((x, y) => x + y) == Right(12))
  }

  "Either map2" should "Left(err2)" in {
    val l2: Either[String, Int] = Left("err2")
    assert(Ch3Exercises.map2(Right(10), l2)((x, y) => x + y) == Left("err2"))
  }

  "Either map2" should "Left(err1)" in {
    val l1: Either[String, Int] = Left("err1")
    assert(Ch3Exercises.map2(l1, Right(2))((x, y) => x + y) == Left("err1"))
  }

  "Either seq" should "List(1, 2, 3)" in {
    assert(Ch3Exercises.sequence(List(Right(1), Right(2), Right(3))) == Right(List(1, 2, 3)))
  }

  "Either seq" should "Left(err)" in {
    assert(Ch3Exercises.sequence(List(Right(1), Left("err"), Right(3))) == Left("err"))
  }

  "Either Traverse" should "Right(List(1, 2, 3))" in {
    assert(Ch3Exercises.traverseEither(List(1, 2, 3))(Right(_)) == Right(List(1, 2, 3)))
  }

  "Either Traverse Err" should "Left(err)" in {
    assert(Ch3Exercises.traverseEither(List(1, 2, 3))(x => if (x == 1) Left("err") else Right(x)) == Left("err"))
  }

}
