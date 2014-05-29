import datastructure.list.{Cons, Nil, List}

object Main extends App {
  val justList = Cons(1, Cons(2, Cons(3, Nil)))
  print(justList.setHead(5))
  print(List.drop(justList, 3))
}