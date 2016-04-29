class Animal()
class Dog() extends Animal
class Cat() extends Animal

object Variance extends App {

  val l: List[Cat] = List(new Cat())

  val ll: List[Animal] = l :+ new Dog()

}
