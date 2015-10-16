package fp_in_scala.exception

import org.scalatest.FlatSpec

class Ch3Spec extends FlatSpec {

  "traverse test" should "return None" in {
    assert(Ch3Exercises.traverse2(List(1, 2, 4))(r => None) == None)
  }

  "traverse test 2" should "return List" in {
    val res = Ch3Exercises.traverse2(List(1, 2, 4))(r => Some(r + 1))
    assert(res == Some(List(2, 3, 5)))
  }

  "traverse test 3" should "return List" in {
    val res = Ch3Exercises.traverse2(List(1, 2, 4))(r => if (r == 2) None else Some(r))
    assert(res == None)
  }

}
