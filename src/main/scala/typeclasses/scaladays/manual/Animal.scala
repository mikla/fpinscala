package typeclasses.scaladays.manual

import typeclasses.scaladays.manual.Eq

sealed trait Animal
case class Cat(name: String, fish: Int) extends Animal
case class Dog(name: String, bones: Int) extends Animal

object Animal {
  implicit def eqAnimal: Eq[Animal] = new Eq[Animal] {
    override def eqv(x: Animal, y: Animal): Boolean = (x, y) match {
//      case (x: Cat, y: Cat) => x === y
//      case (x: Dog, y: Dog) => x === y
      case _ => false
    }
  }
}

object Cat {
   implicit val eqCat = new Eq[Cat] {
     override def eqv(x: Cat, y: Cat): Boolean = {
       x.name === y.name && x.fish === y.fish
     }
   }
}

object Dog {
  implicit val eqDog = new Eq[Dog] {
    override def eqv(x: Dog, y: Dog): Boolean = {
      x.name === y.name && x.bones === y.bones
    }
  }
}
