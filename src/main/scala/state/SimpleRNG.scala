package state

case class SimpleRNG(seed: Long) extends RNG {
  override def nextInt: (Int, RNG) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRNG = SimpleRNG(newSeed)
    val n = (newSeed >>> 16).toInt
    (n, nextRNG)
  }
}

object SimpleRNG {
  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (value, rngState) = rng.nextInt
    if (value < 0) (-(value + 1), rngState) else (value, rngState)
  }

  def double(rng: RNG): (Double, RNG) = {
    val (value, rngState) = nonNegativeInt(rng)
    (value / (Int.MaxValue.toDouble + 1), rngState)
  }

  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (i1, r1) = nonNegativeInt(rng)
    val (d1, r2) = double(r1)
    ((i1, d1), r2)
  }

  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val r = intDouble(rng)
    (r._1.swap, r._2)
  }

  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (d1, r1) = double(rng)
    val (d2, r2) = double(r1)
    val (d3, r3) = double(r2)
    ((d1, d2, d3), r3)
  }

}
