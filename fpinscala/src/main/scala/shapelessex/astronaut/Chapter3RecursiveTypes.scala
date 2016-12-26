package shapelessex.astronaut

import shapelessex.astronaut.core.CsvEncoder
import shapelessex.astronaut.core.defaultEncoders._
import CsvEncoder._

object Chapter3RecursiveTypes extends App {

  case class Bar(baz: Int, qux: String)
  case class Foo(bar: Bar)

  sealed trait Tree[T]
  case class Branch[T](left: Tree[T], right: Tree[T]) extends Tree[T]
  case class Leaf[T](value: T) extends Tree[T]

  implicitly[CsvEncoder[Foo]] // we have to wrap head of HList to Lazy while deriving CsvEncoder instance
  implicitly[CsvEncoder[Tree[Int]]] // same. it'c complex type


}
