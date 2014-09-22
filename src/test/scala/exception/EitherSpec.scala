package exception

import org.scalatest.FlatSpec

class EitherSpec extends FlatSpec {

  "Either map" should "return ?? just checking )" in {
      val l: Left[String, Int] = Left("str")
//      println(l.left.map())
//    assert()
  }

  "Either flatMap on Left" should "LeftType(scala)" in {
    assert(LeftType(12).flatMap(x => LeftType("scala")) == LeftType("scala"))
  }

}
