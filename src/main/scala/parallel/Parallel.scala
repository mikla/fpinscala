package parallel

object Parallel extends App {
  def sum(ints: IndexedSeq[Int]): Int = {
    if (ints.size <= 1) ints.headOption getOrElse 0
    else {
      val (l, r) = ints.splitAt(ints.length / 2)
      val sumL = Par.unit(sum(l))
      val sumR = Par.unit(sum(r))
      Par.run(sumL) + Par.run(sumR) // Par.get blocks execution here!
    }
  }

  /**
   * Avoiding pitfalls of combining unit and get.
   */
  def sum2(ints: IndexedSeq[Int]): Par[Int] = {
    if (ints.size <= 1) Par.unit(ints.headOption getOrElse 0)
    else  {
      val (l, r) = ints.splitAt(ints.length / 2)
      Par.map2(sum2(l), sum2(r))(_ + _)
    }
  }

  /**
   * Control parallelism via fork
   * @param ints
   * @return
   */
  def sum3(ints: IndexedSeq[Int]): Par[Int] = {
    if (ints.size <= 1) Par.unit(ints.headOption getOrElse 0)
    else  {
      val (l, r) = ints.splitAt(ints.length / 2)
      Par.map2(Par.fork(sum3(l)), Par.fork(sum3(r)))(_ + _)
    }
  }

}
