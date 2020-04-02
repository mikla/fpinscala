package stuff

object Binary extends App {

  import Vect._

  trait Vect extends Any {
    type Item
    def length: Int
    def get(index: Int): Item
    def set(index: Int, item: Item): Aux[Item]
  }

  object Vect {
    type Aux[I] = Vect {type Item = I}
  }

  final case class BoolVect64(values: Long) extends AnyVal with Vect {
    type Item = Boolean
    def length = 64
    def get(index: Int) = ((values >> index) & 1) == 1
    def set(index: Int, item: Boolean) = BoolVect64(
      if (item) values | (1L << index) else values & ~(1L << index)
    )
  }

  final case class BoolVect8(values: Byte) extends AnyVal with Vect {
    type Item = Boolean
    def length = 8
    def get(index: Int) = ((values >> index) & 1) == 1
    def set(index: Int, item: Boolean) = BoolVect8(
      (if (item) values | (1 << index) else values & ~(1 << index)).toByte
    )
  }

  def toList(vect: Vect): List[vect.Item] = (0 until vect.length).map(i => vect.get(i)).toList

  val x: Long = 1234

  val padded: String = x.toBinaryString.reverse.padTo(64, '0').reverse

  val xx = x.toBinaryString

  println(xx)

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

  println("Bool 8")


  println(BoolVect64(123).get(0))
  println(BoolVect64(123).get(1))
  println(BoolVect64(123).get(2))
  println(BoolVect64(123).get(3))
  println(BoolVect64(123).get(4))
  println(BoolVect64(123).get(5))
  println(BoolVect64(123).get(6))
  println(BoolVect64(123).get(7))

  println(BoolVect8(123).set(1, false).values)




}
