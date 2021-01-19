package stuff.hackerrank

import scala.io.Source

object CompareThePriplets extends App {
  val input = Source.stdin.getLines().take(2).toList
  val a = input.head.split(" ").map(_.toInt)
  val b = input(1).split(" ").map(_.toInt)

  val (_, (alice, bob)) = a.foldLeft((b, (0, 0))) { (acc, elem) =>
    if (elem > acc._1.head) (acc._1.tail, (acc._2._1 + 1, acc._2._2))
    else if (elem < acc._1.head) (acc._1.tail, (acc._2._1, acc._2._2 + 1))
    else (acc._1.tail, acc._2)
  }

  println(s"$alice $bob")

}
