package shapelessex

package object astronaut {

  // ADT (algebraic data types)
  sealed trait Shape

  final case class Rectangle(width: Double, height: Double) extends Shape
  final case class Circle(radius: Double) extends Shape

  val AllShapes = List(Rectangle(1, 1), Circle(1))

}
