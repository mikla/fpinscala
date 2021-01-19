package shapelessex.astronaut

import shapeless._

object Chapter2Derivation extends App {

  val rect: Shape = Rectangle(3.0, 4.0)
  val circ: Shape = Circle(1.0)

  def area(shape: Shape): Double =
    shape match {
      case Rectangle(w, h) => w * h
      case Circle(r) => math.Pi * r * r
    }

  area(rect)

  // we can also define Products and Coproducts in terms of tuples and Either

  type Rectange2 = (Double, Double)
  type Circle2 = Double

  type Shape2 = Either[Rectange2, Circle2]

  // but tuples are not suitable for generic programming for several reasons

  val product: String :: Int :: Boolean :: HNil = "Sunday" :: 1 :: false :: HNil

  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

  val iceCream = IceCream("Top", 1, true)

  val genericIceCream = Generic[IceCream]

  // Generic has type member Repr, which contains representation type

  val iceCreamRepr = genericIceCream.to(iceCream)

  val iceCream2 = genericIceCream.from(iceCreamRepr)

  // Generic coproducts

  case class Red()
  case class Amber()
  case class Green()

  type Light = Red :+: Amber :+: Green :+: CNil

  val red: Light = Inl(Red())
  val green: Light = Inr(Inr(Inl(Green())))

  val genCoproduct = Generic[Shape] // Rectangle :+: Circle :+: CNil
  val rectRepr = genCoproduct.to(Rectangle(1.0, 1.0)) // Inr(Inl(Rectangle())
  println(rectRepr)

}
