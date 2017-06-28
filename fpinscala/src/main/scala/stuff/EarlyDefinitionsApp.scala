package stuff

trait WithX[T] {
  val x: T
}

class WithXImpl(xx: Int) extends { val x: Int = xx } with WithX[Int]

class WithXImpl2(xx: Int) extends WithX[Int] {
  override val x: Int = xx
}

object EarlyDefinitionsApp extends App {

  println(new WithXImpl(23).x)
  println(new WithXImpl2(23).x)

}
