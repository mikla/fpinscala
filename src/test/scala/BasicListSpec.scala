import org.scalatest.FlatSpec

class BasicListSpec extends FlatSpec {

  "foldLeft Nil:List[Int]" should "return reversed list" in {

    val list = List(1, 2)
    val newList = list.foldLeft(Nil: List[Int])((x, y) => ::(y, x))
    assert(newList.head == 2)
  }

}
