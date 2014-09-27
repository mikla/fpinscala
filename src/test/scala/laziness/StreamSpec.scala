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

}
