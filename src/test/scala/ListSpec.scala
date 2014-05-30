import org.scalatest.FlatSpec
import datastructure.list.{Cons, List, Nil}

class ListSpec extends FlatSpec {

  "List folding sum" should "return sum of alements" in {
    val lst = List(1, 2, 3, 4, 5, 6)
    assert(List.foldRight(lst, 0)(_ + _) == 21)
  }

  "List folding product" should "return sum of alements" in {
    val lst = List(1, 2, 3, 4, 5, 6)
    assert(List.foldRight(lst, 1)(_ * _) == 720)
  }

  "Passing Nil:List[Int]" should "return exactly the same list" in {
    println(List.foldRight(Cons(1, Cons(2, Nil)), Nil: List[Int])(Cons (_, _)))
  }



}
