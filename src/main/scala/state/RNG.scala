package state

trait RNG {
  def nextInt: (Int, RNG)
}
