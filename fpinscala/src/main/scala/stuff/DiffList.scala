package stuff

object DiffList extends App {

  class DiffList[A](calculate: List[A]) {
    def prepend(s: List[A]): DiffList[A] = new DiffList(s ::: calculate)

    def append(s: List[A]): DiffList[A] = new DiffList(calculate ::: s)

    def result: List[A] = calculate
  }

  // Nil
  val append: List[Int] => List[Int] = x => x ::: List(1, 2, 3)
  val prepend = append.andThen(List(4, 5, 6) ::: _)
  val append2 = prepend.andThen(_ ::: List(10, 11, 12))

  var start = identity(Nil)

  List(1, 2, 3) ::: List(4, 5, 6)

  println(append2(Nil))

  val l1 = List(1, 2, 3)
  val l2 = List(4, 5, 6)
  val dl = new DiffList[Int](Nil)

  val result = dl.append(l2).prepend(l1).result

  println(result)
}
