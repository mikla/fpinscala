package stuff.hackerrank

object JumpingOnTheClouds extends App {

  def jumpingOnClouds(c: Array[Int]): Int = {

    def minPath(length: Int, tail: List[Int]): Int = {
      tail match {
        case 1 :: 1 :: _ => length
        case 0 :: 1 :: rest => minPath(length + 1, 1 :: rest)
        case 1 :: 0 :: rest => minPath(length + 1, rest)
        case 0 :: 0 :: rest => Math.min(minPath(length + 1, 0 :: rest), minPath(length + 1, rest))
        case 0 :: rest => minPath(length + 1, rest)
        case Nil => length
      }
    }

    minPath(1, c.toList.tail) - 1
  }

  println(jumpingOnClouds(Array(0, 0, 1, 0, 0, 1, 0)))
  println(jumpingOnClouds(Array(0, 0, 0, 0, 1, 0)))
  println(jumpingOnClouds(Array(0, 0, 0, 1, 0, 0)))

}
