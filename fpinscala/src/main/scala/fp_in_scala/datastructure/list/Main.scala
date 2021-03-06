package fp_in_scala.datastructure.list

object Main extends App {
  val justList = Cons(1, Cons(2, Cons(3, Nil)))
  println(justList.setHead(5))
  println(List.drop(justList, 3))

  val dropWhileList: List[Int] = List(-1, -2, 3)
  println(List.dropWhile(dropWhileList)(x => x < 0))

  val l1 = List(1, 2, 3, 4)
  val l2 = List(5, 6, 7, 8)
  println(List.append(l1, l2))

  val initList = List(1, 2, 3, 4)
  println(List.init(initList))

  println(List.reverse(initList))

  println(List.startsWith(List(1, 2, 3, 4), List(1, 2, 3, 4)))

  val divideList = List(1, 2, 3)
  println(List.foldLeft(divideList, 1)((a, b) => a / b))

}
