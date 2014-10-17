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
}
