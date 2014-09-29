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

  "Stream.map" should "Stream(11, 12)" in {
    assert(Stream(1, 2).map(_ + 10).toList == List(11, 12))
  }

  "Stream.map" should "Empty" in {
    assert(Empty.asInstanceOf[Stream[Int]].map(_ + 10) == Empty)
  }

  "Stream.filter" should "Stream(2, 4)" in {
    assert(Stream(1, 2, 3, 4).filter(_ % 2 == 0).toList == List(2, 4))
  }

  "Stream.filter" should "Empty" in {
    assert(Stream(1, 2, 3, 4).filter(_ > 10) == Empty)
  }

  "Stream.flatMap" should "Stream(1, 1, 2, 2, 3, 3)" in {
    val s = Stream(1, 2, 3)
    val fMapRes = s.flatMap(x => Stream(x, x)).toList
    assert(fMapRes.head == 1 && fMapRes.size == 6)
  }

}
