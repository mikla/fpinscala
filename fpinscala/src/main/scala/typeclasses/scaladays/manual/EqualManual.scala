package typeclasses.scaladays.manual

object EqualManual extends App {

  val cat = Cat("cat", 1)
  val cat1 = Cat("cat", 2)
  cat === cat1
  println(cat === cat1)

  val dog = Dog("dog", 1)
  val dog1 = Dog("dog", 1)
  println(dog === dog1)

}
