package shapelessex

import shapeless.{:+:, CNil, Coproduct, Poly1}

object CoproductApp extends App {

  type ISB = Int :+: String :+: Boolean :+: CNil

  val isb = Coproduct[ISB]("foo")

  println(isb)
  println(isb.select[Int])

  object sizeM extends Poly1 {
    implicit def caseInt = at[Int](i => (i, i))
    implicit def caseString = at[String](s => (s, s.length))
    implicit def caseBoolean = at[Boolean](b => (b, 1))
  }

  // coproduct supports mapping given a poly function

  val mappedIsb = isb.map(sizeM)

  println(mappedIsb)

  println(mappedIsb.select[(String, Int)])

}
