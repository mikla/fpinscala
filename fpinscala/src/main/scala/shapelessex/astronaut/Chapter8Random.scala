package shapelessex.astronaut

import shapeless.{::, Generic, HList, HNil, Lazy}

trait Random[A] {
  def get: A
}

object Random {

  implicit def genericRandomCase[A, R <: HList](
    implicit
    gen: Generic.Aux[A, R],
    rand: Lazy[Random[R]]
  ): Random[A] = new Random[A] {
    override def get: A = gen.from(rand.value.get)
  }

  implicit val hnilRandom: Random[HNil] = new Random[HNil] {
    override def get = HNil
  }

  implicit def hlistRandom[H, T <: HList](
    implicit
    hRand: Lazy[Random[H]],
    tRand: Random[T]
  ): Random[H :: T] = new Random[H :: T] {
    override def get = hRand.value.get :: tRand.get
  }

  implicit val intRandom = new Random[Int] {
    override def get: Int = scala.util.Random.nextInt()
  }

  implicit val charRandom = new Random[Char] {
    override def get: Char = scala.util.Random.nextInt().toChar
  }

  implicit val booleanRandom = new Random[Boolean] {
    override def get: Boolean = scala.util.Random.nextBoolean()
  }

  implicit val stringRandom = new Random[String] {
    override def get: String = scala.util.Random.nextString(10)
  }

}

object Chapter8Random extends App {

  def random[A](implicit R: Random[A]): A = R.get

  println(random[Employee])

}
