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
    List.foldRight(Cons(1, Cons(2, Nil)), Nil: List[Int])(Cons (_, _))
  }

  "foldLeft Nil:List[Int]" should "return reversed list" in {
    val newList = List.foldLeft2(Cons(1, Cons(2, Nil)), Nil: List[Int])((x, y) => Cons (y, x))
    assert(newList.head == 2)
  }

  "Checking length method" should "return size of List" in {
    val lst = List(1, 2, 3, 4, 5, -6)
    assert(List.length(lst) == 6)
  }

  "Checking length method Nil passed" should "return 0" in {
    assert(List.length(Nil) == 0)
  }


  "foldRightViaFoldLeft" should "return exactly the same list" in {
    assert(List.foldRightViaFoldLeft(Cons(1, Cons(2, Nil)), Nil: List[Int])(Cons (_, _)).head == 1)
  }

  "foldLeft via foldRight" should "return exactly the same list" in {
    assert(List.foldLeftViaFoldRight(Cons(1, Cons(2, Nil)), Nil: List[Int])((x, y) => Cons (y, x)).head == 2)
  }

  "appending list to the end" should "return concatenated list" in {
    val list1 = List(1, 2)
    val list2 = List(3, 4)
    val newList = List.append(list1, list2)
    assert(newList.head == 1 && List.length(newList) == 4)
  }

  "appending via foldLeft list to the end " should "return concatenated list" in {
    val list1 = List(1, 2)
    val list2 = List(3, 4)
    val newList = List.appendViaFoldRight(list1, list2)
    assert(newList.head == 1 && List.length(newList) == 4)
  }

  "concat List[List[T]]" should "return List[T]" in {
    val lst = List(List(1, 2, 3), List(4), List(5,6,7))
    val ans = List.concat(lst)
    assert(List.length(ans) == 7 && ans.head == 1)
  }

  "incEach" should "return incremented by 1 List" in {
    val lst = List(1, 2, 3)
    val newList = List.incEach(lst)
    assert(newList.head == 2 && List.length(newList) == 3)
  }

  "toStringEach" should "return List[String]" in {
    val lst = List(1.0, 2.0, 3.0)
    val newList = List.toStringEach(lst)
    assert(newList.head.toDouble == "1".toDouble)
  }


  "List.map " should "return List[B] where f applied  to each element" in {
    val lst = List(1, 2, 3)
    val newList = List.map(lst)(_ * 2)
    assert(newList.head == 2)
  }

  "List.filter " should "removes elements from a list filter unless they satisfy a given predicate." in {
    val lst = List(0, 1, 2, 3, -1)
    val newList = List.filter(lst)(_ > 0)
    assert(newList.head == 1)
  }

  "List.flatMap " should "and that list should be\ninserted into the final resulting list" in {
    val lst = List(1, 2, 3)
    val newList = List.flatMap(lst)(x => List(x, x))
    assert(newList.head == 1 && List.length(newList) == 6)
  }


  "List.filterViaFlatMap" should "removes elements from a list filter unless they satisfy a given predicate." in {
    val lst = List(0, 1, 2, 3, -1)
    val newList = List.filterViaFlatMap(lst)(_ > 0)
    assert(newList.head == 1)
  }

  "hasSubsequence List(1, 2, 3, 4) and List(3, 4)" should "returns true" in {
    val list = List(1, 2, 3, 4)
    assert(List.hasSubsequence(list, List(3, 4)))
  }

  "hasSubsequence List(1, 2, 3, 1, 2, 3, 4) and List(1, 2, 3, 4)" should "returns true" in {
    val list = List(1, 2, 3, 1, 2, 3, 4)
    assert(List.hasSubsequence(list, List(1, 2, 3, 4)))
  }

  "hasSubsequence List(1, 2, 3, 1, 2, 3, 4) and List(5, 6)" should "returns false" in {
    val list = List(1, 2, 3, 1, 2, 3, 4)
    assert(!List.hasSubsequence(list, List(5, 6)))
  }

  "hasSubsequence List(1, 2) and List(5, 6, 7)" should "returns false" in {
    val list = List(1, 2)
    assert(!List.hasSubsequence(list, List(5, 6, 7)))
  }

  "hasSubsequence Nil and Nil" should "return true" in {
    assert(List.hasSubsequence(Nil, Nil))
  }

  "hasSubsequence List(1, 2) and Nil" should "returns false" in {
    assert(List.hasSubsequence(List(1, 2), Nil))
  }

  "hasSubsequence Nil and List(1, 2)" should "returns false" in {
    assert(!List.hasSubsequence(Nil, List(1, 2)))
  }

  "hasSubsequence List(0, 1, 2, 1, 2, 1, 2) and List(1, 2)" should "return true" in {
    assert(List.hasSubsequence(List(0, 1, 2, 1, 2, 1), List(1, 2)))
  }

  "startsWith List(0, 1, 2, 1, 2, 1, 2) and List(1, 2)" should "returns false" in {
    assert(!List.startsWith(List(0, 1, 2, 1, 2, 1), List(1, 2)))
  }

  "startsWith List(0, 1, 2, 1, 2, 1, 2) and List(0, 1)" should "returns true" in {
    assert(List.startsWith(List(0, 1, 2, 1, 2, 1), List(0, 1)))
  }

  "startsWith Nil and List(0, 1)" should "returns false" in {
    assert(!List.startsWith(Nil, List(0, 1)))
  }

  "startsWith List(1, 2) and Nil" should "returns false" in {
    assert(List.startsWith(List(1, 2), Nil))
  }

}
