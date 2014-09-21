package exception

import org.scalatest.FlatSpec

class EitherSpec extends FlatSpec {

  "Either map" should "return ?? just checking )" in {
      val l: Either[String, Int] = Left("str")
      val s = l
//    assert()
  }

}
