package scalaz.applicative

object Appl extends App {

  /*
    trait Functor[C[_]] {
      def map[A, B](f: A => B): C[A] => C[B]
    }

    trait Applicative[F[_]] {
      def apply[A, B](f: F[A => B]): F[A] => F[B]
    }
  */

  /*
    (A => B) => (C[A] => C[B]) - Functor
    (A => C[B]) => (C[A] => C[B]) - Monad
    (C[A => B]) => (C[A] => C[B]) - Applicative
  */


  class Box[T](val value: T)
  val box1: Box[String] = new Box("box1")

  // let's define functor
  def map[A, B](f: A => B): Box[A] => Box[B] = (a: Box[A]) => new Box(f(a.value))

  def rawLengthOf(a: String) = a.length
  def rawSquareOf(a: Int) = a * a
  def transformationLength = map(rawLengthOf) // Box[String] => Box[Int]

  val functorResult = transformationLength(box1) // Box[Int]

  // let's define monad
  def flatMap[A, B](f: A => Box[B]): Box[A] => Box[B] = (a: Box[A]) => f(a.value)
  def lengthOf(a: String) = new Box(a.length)
  def squareOf(a: Int) = new Box(a * a)

  def transformationLenght = flatMap(lengthOf)
  def transformationSquare = flatMap(squareOf)

  def combinedTransformation = transformationLenght andThen transformationSquare

  println(combinedTransformation(box1).value)
  println(transformationSquare(transformationLenght(box1)).value)

  // let's do applicative stuff!
  val boxedLengthOf: Box[String => Int] = new Box(rawLengthOf)
  val boxedSquareOf: Box[Int => Int] = new Box(rawSquareOf)
  def apply[A, B](b: Box[A => B]): Box[A] => Box[B] = (a: Box[A]) => new Box(b.value(a.value))

  val apLength = apply(boxedLengthOf)
  val apSquare = apply(boxedSquareOf)

  println(apSquare(apLength(box1)).value)


  // Scalaz example
  val res11 = List(1, 2, 3, 4) map {(_: Int) * (_: Int)}.curried
  println(res11 map (_(9)))

}
