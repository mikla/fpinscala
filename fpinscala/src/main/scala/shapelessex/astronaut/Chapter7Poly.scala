package shapelessex.astronaut

import shapeless.Poly1
import shapeless._

object Chapter7Poly extends App {

  object sizeOf extends Poly1 {
    implicit val intCase: Case.Aux[Int, Int] = at(identity)
    implicit val stringCase: Case.Aux[String, Int] = at(_.length)
    implicit val boolCase: Case.Aux[Boolean, Int] = at(a => if (a) 1 else 0)
  }

  println((12 :: "hello" :: true :: HNil).map(sizeOf))

  // flatMap over HList is similar, just return HList from at()

  // folding

  object sum extends Poly2 {
    implicit val intIntCase: Case.Aux[Int, Int, Int] = at((a, b) => a + b)
    implicit val intStringCase: Case.Aux[Int, String, Int] = at((a, b) => a + b.length)
  }

  println((1 :: 2 :: "str" :: "str" :: HNil).foldLeft(0)(sum))

}
