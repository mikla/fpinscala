package stuff

import cats.implicits._

object Binary2 extends App {

  import Vect.Aux

  trait Vect extends Any {
    type Item
    def length: Int
    def get(index: Int): Item
    def set(index: Int, item: Item): Aux[Item]
  }

  object Vect {
    type Aux[I] = Vect { type Item = I }
  }

  final case class BoolVect64(values: Long) extends AnyVal with Vect {
    type Item = Boolean
    def length = 64
    def repr: String = values.toBinaryString.reverse.padTo(64, '0')
    def get(index: Int): Boolean = repr(index) == '1'
    def set(index: Int, item: Boolean) =
      BoolVect64(BigInt(repr.updated(index, if (item) '1' else '0').reverse.mkString, 2).toLong)
  }

  final case class BoolVect8(values: Byte) extends AnyVal with Vect {
    type Item = Boolean
    def length = 8
    def repr = values.toBinaryString.reverse.padTo(8, '0')
    def get(index: Int) = repr(index) == '1'
    def set(index: Int, item: Boolean) = BoolVect8(
      BigInt(repr.updated(index, if (item) '1' else '0').reverse.mkString, 2).toByte
    )
  }

  def toList(vect: Vect): List[vect.Item] = (0 until vect.length).map(i => vect.get(i)).toList

  println(toList(BoolVect64(1234)))
  println(BoolVect64(1234).get(0))
  println(BoolVect64(1234).get(1))
  println(BoolVect64(1234).get(2))
  println(BoolVect64(1234).get(3))
  println(BoolVect64(1234).get(4))
  println(BoolVect64(1234).get(5))
  println(BoolVect64(1234).get(6))
  println(BoolVect64(1234).get(7))

  println(BoolVect64(1234).set(1, false).values)

  println("== Bool 8 ==")

  println(123.toBinaryString)
  println(123.toBinaryString.padTo(8, '0'))

  println(BoolVect64(123).get(0))
  println(BoolVect64(123).get(1))
  println(BoolVect64(123).get(2))
  println(BoolVect64(123).get(3))
  println(BoolVect64(123).get(4))
  println(BoolVect64(123).get(5))
  println(BoolVect64(123).get(6))
  println(BoolVect64(123).get(7))

  println(BoolVect8(123).set(1, false).values)

  //  println(toList(BoolVect8(1)))
  //  println(BoolVect8(1).get(6))

  val x: (List[Int], List[String]) = (Nil, Nil)

  val absences: List[Boolean] = Nil

  val xx = absences.map(absense => x).toMap

  val xx1 = absences.map(absense => x).reduce(_ combine _)

}
