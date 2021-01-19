package stuff.hackerrank

object CountingValleysApp extends App {

  case class P(height: Int, valleys: Int)

  def countingValleys(n: Int, s: String): Int =
    s.foldLeft(P(0, 0)) { case (acc, c) =>
      val acc_h = c match {
        case 'D' => acc.copy(height = acc.height - 1)
        case 'U' => acc.copy(height = acc.height + 1)
      }
      if (acc_h.height == 0 && c == 'U') acc_h.copy(valleys = acc_h.valleys + 1)
      else acc_h
    }.valleys

  println(countingValleys(8, "DDUUUUDD"))

}
