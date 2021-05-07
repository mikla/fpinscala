package fp_in_scala.property

import cats.data.State
import cats.implicits._

import scala.util.Random

case class Gen[T](sample: State[Random, T]) {

  def flatMap[B](f: T => Gen[B]): Gen[B] =
    Gen(sample.flatMap(t => f(t).sample))

  def listOfN(size: Gen[Int]): Gen[List[T]] =
    size.flatMap(s => Gen.listOfN(s, this))

}

object Gen {

  /** Actually, this union looks weird. I would want to have Moniod[A] to combine them */
  def union[A](g1: Gen[A], g2: Gen[A]): Gen[A] = ???

  def choose(start: Int, stopExclusive: Int): Gen[Int] =
    Gen(State(seed => (seed, seed.between(start, stopExclusive))))

  def listOf[A](a: Gen[A]): Gen[List[A]] = ???

  def listOfN[A](size: Int, a: Gen[A]): Gen[List[A]] = Gen(List.fill(size)(a.sample).sequence)

  def forAll[A](a: Gen[A])(p: A => Boolean): Prop = ???

  def unit[A](a: => A): Gen[A] = Gen(State(s => (s, a)))

  def boolean: Gen[Boolean] = Gen(State(seed => (seed, seed.nextBoolean())))

}
