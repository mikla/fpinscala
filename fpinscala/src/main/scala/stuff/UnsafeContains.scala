package stuff

object UnsafeContains extends App {

  List(1, 2, 3).contains("1")

  class A
  class B
  val x =
    if (true) new A {}
    else new B
  List.empty[A].contains(new B {})

}
