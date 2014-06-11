import org.scalatest.FlatSpec

class BasicListSpec extends FlatSpec {

  "foldLeft Nil:List[Int]" should "return reversed list" in {

    val list = List(1, 2)
    val newList = list.foldLeft(Nil: List[Int])((x, y) => ::(y, x))
    assert(newList.head == 2)
  }


  "foldRight Nil:List[Int]" should "return reversed list" in {
    val list = List(1, 2)
    val newList = list.foldRight(Nil: List[Int])((x, y) => ::(x, y))
    assert(newList.head == 1)
  }

  "flatMap" should "return List[T]" in {
    val list = List(1, 2, 3)
    val newList = list.flatMap(x => List(x, x))
    assert(newList.head == 1 && newList.length == 6)
  }

  "startsWith List(1, 2) and Nil" should "return ?" in {
    val list = List(1, 2)
    assert(list.startsWith(Nil))
  }

  "reduce List" should "return ?" in {
    val list = List(1, 2)
    assert(list.startsWith(Nil))
  }

}
