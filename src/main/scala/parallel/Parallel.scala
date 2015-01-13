package parallel

object Parallel extends App {
  def sum(ints: IndexedSeq[Int]): Int = {
    if (ints.size <= 1) ints.headOption getOrElse 0
    else {
      val (l, r) = ints.splitAt(ints.length / 2)
      val sumL = Par.unit(sum(l))
      val sumR = Par.unit(sum(r))
      Par.get(sumL) + Par.get(sumR)
    }
  }

}
