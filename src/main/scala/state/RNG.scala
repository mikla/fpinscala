package state

trait RNG {

  type Rand[+A] = RNG => (A, RNG)

  def nextInt: (Int, RNG)

  def int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] = rng => (a, rng)

}
