import org.scalatest.FlatSpec
import datastructure.list.{Cons, List, Nil}

class ListSpec extends FlatSpec {

  "List folding sum" should "return sum of alements" in {
    val lst = List(1, 2, 3, 4, 5, 6)
    assert(List.foldRight(lst, 0)(_ + _) == 21)
  }

  "List folding left sum" should "return sum of elements" in {
    val lst = List(1, 2, 3, 4, 5, 6)
    assert(List.foldLeft(lst, 0)(_ + _) == 21)
  }

  "List folding product" should "return sum of alements" in {
    val lst = List(1, 2, 3, 4, 5, 6)
    assert(List.foldRight(lst, 1)(_ * _) == 720)
  }

  "List.foldLeft folding product" should "return sum of alements" in {
    val lst = List(1, 2, 3, 4, 5, 6)
    assert(List.foldLeft(lst, 1)(_ * _) == 720)
  }

  "Passing Nil:List[Int]" should "return exactly the same list" in {
    println(List.foldRight(Cons(1, Cons(2, Nil)), Nil: List[Int])(Cons (_, _)))
  }

  "Checking length method" should "return size of List" in {
    val lst = List(1, 2, 3, 4, 5, -6)
    assert(List.length(lst) == 6)
  }

  "Checking length method Nil passed" should "return 0" in {
    assert(List.length(Nil) == 0)
  }

}
