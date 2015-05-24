package property

import scala.util.Random
import scalaz.State

case class Gen[A](sample: State[RNG, A]) {
  def listOfN(n: Int, a: Gen[A]): Gen[List[A]] = ???
}

object Gen {
  def forAll[A](a: Gen[A])(f: A => Boolean): Prop = ???
  def choose(start: Int, stopExclusive: Int): Gen[Int] = ??? /*{
    Gen(State(SimpleRNG.nonNegativeInt).)
  }*/
}


