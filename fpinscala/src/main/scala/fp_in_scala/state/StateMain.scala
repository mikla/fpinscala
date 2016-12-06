package fp_in_scala.state

import scala.util.Random

object StateMain extends App {
  val rng = new Random
  println(rng.nextDouble())

  // functional random generation

  val rngFunc = SimpleRNG(42)
  val (n1, rngFunc2) = rngFunc.nextInt
  println(n1)
  val (n2, rngFunc3) = rngFunc2.nextInt
  println(n2)

}
