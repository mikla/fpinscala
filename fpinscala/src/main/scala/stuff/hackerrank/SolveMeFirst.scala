package stuff.hackerrank

import scala.io.Source

object SolveMeFirst {

  def main(args: Array[String]) = {

    println {
      Source.stdin.getLines().take(2).map(_.toInt).sum
    }

  }

}
