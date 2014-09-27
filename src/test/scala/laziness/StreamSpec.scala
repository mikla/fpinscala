package laziness

import org.scalatest.FlatSpec


class StreamSpec extends FlatSpec {

  "Stream.toList" should "List(1, 2, 3)" in {
    assert(Stream(1, 2, 3).toList == List(1, 2, 3))
  }

  "Stream.take(n)" should "List(1, 2, 3)" in {
    assert(Stream(1, 2, 3, 4, 5, 6).take(3).toList == List(1, 2, 3))
  }

  "Stream.drop(n)" should "List(4, 5, 6)" in {
    assert(Stream(1, 2, 3, 4, 5, 6).drop(3).toList == List(4, 5, 6))
  }

  "Stream.reverse" should "Stream(3, 2, 1)" in {
    assert(Stream(1, 2, 3).reverse.toList == Stream(3, 2, 1).toList)
  }

  "Stream.takeWhile" should "Stream(1, 1)" in {
    assert(Stream(1, 1, 3).takeWhile(_ == 1).toList == Stream(1, 1).toList)
  }

  "Stream.takeWhileViaFoldRight" should "Stream(1, 1)" in {
    assert(Stream(1, 1, 3).takeWhileViaFoldRight(_ == 1).toList == Stream(1, 1).toList)
  }

  "Stream.exists" should "true" in {
    assert(Stream(1, 1, 3).exists(_ == 3))
  }

  "Stream.exists" should "false" in {
    assert(!Stream(1, 1, 3).exists(_ == 4))
  }

  "Stream.forAll" should "true" in {
    assert(Stream(1, 1, 1).forAll(_ == 1))
  }

  "Stream.forAll" should "false" in {
    assert(!Stream(1, 1, 3).forAll(_ == 1))
  }

  "Stream.headOptionViaFoldRIght" should "Some(1)" in {
    assert(Stream(1, 2, 3).headOptionViaFoldRight == Some(1))
  }

  "Stream.headOptionViaFoldRIght" should "None" in {
    assert(Empty.headOptionViaFoldRight == None)
  }
}
