package scalaz.state

import scalaz._

object PlayingStateMonad extends App with StateFunctions {

  val m1 = State { s: String => (s, s.size)}

  def repeat(num: Int): State[String, Unit] = State { s: String => (s * num, ()) }

  val x = m1.run("s")

  println(m1.flatMap(repeat).run("hello"))

  println(m1.flatMap(repeat).flatMap(_ => m1).run("33"))

  get[String]
    .flatMap( s0 => repeat(s0.size) )
    .flatMap({ _ => get[String]})
    .map({ s1 => s1.size })
    .run("hello")

  val m = for {
    s0 <- get[String]
    _  <- put(s0 * s0.size)
    s1 <- get[String]
  } yield s1.size

  m.run("hello")
}
