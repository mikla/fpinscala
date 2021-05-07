package shapelessex

import shapeless._
import shapeless.poly._
import shapeless.syntax.zipper._
import shapelessex.PolyApp.size

object HListApp extends App {

  object choose extends (Set ~> Option) {
    override def apply[T](f: Set[T]): Option[T] = f.headOption
  }

  val sets = Set(1) :: Set("foo") :: HNil

  val opts = sets.map(choose) // Some(1) :: Some("foor") :: HNil

  val l = (23 :: "foo" :: HNil) :: HNil :: (true :: HNil) :: HNil

  val fl = l.flatMap(identity)

  // 23 :: "foo" :: HNil

  // fold

  object addSize extends Poly2 {

    implicit def default[T](implicit st: shapelessex.PolyApp.size.Case.Aux[T, Int]) =
      at[Int, T]((acc, t) => acc + size(t))

  }

  val s1 = 23 :: "foo" :: (13, "wibble") :: HNil

  val size1 = s1.foldLeft(0)(addSize)

  println(size1)

  // zipper
  {
    val l = 1 :: "foo" :: 3.0 :: HNil
    println(l.toZipper.right.put(("wibble", 45)).reify)
    println(l.toZipper.right.delete.reify)
  }

  // covariance

  trait Fruit

  case class Apple() extends Fruit

  case class Pear() extends Fruit

  type FFFF = Fruit :: Fruit :: Fruit :: Fruit :: HNil
  type APAP = Apple :: Pear :: Apple :: Pear :: HNil

  val a: Apple = Apple()
  val p: Pear = Pear()

  val apap: APAP = a :: p :: a :: p :: HNil

  println(apap.isInstanceOf[FFFF])
  val xd: List[Fruit] = apap.toList

  // casting
  import syntax.typeable._

  val ffff: FFFF = apap.unify
  val precise: Option[APAP] = ffff.cast[APAP]

  println(precise)

}
