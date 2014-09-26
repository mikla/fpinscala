package laziness

import org.scalatest.FlatSpec


class StreamSpec extends FlatSpec {

  "Stream.toList" should "List(1, 2, 3)" in {
    assert(Stream(1, 2, 3).toList == List(1, 2, 3))
  }

  "Stream.take(n)" should "List(1, 2, 3)" in {
    assert(Stream(1, 2, 3, 4, 5, 6).take(3).toList == List(1, 2, 3))
  }

}
