package stuff

import cats.Eq
import cats.implicits._

object SortByApp extends App {

  println {

    List(1, 4, 8, 9, 5, 1, 17, 10)
      .sortBy(i => (i % 2 != 0, i))

  }

  println {
    List(1, 2).fold(List.empty[Int]) {
      case (Nil, elem) => List(elem)
      case (acc, elem) => acc
    }
  }

  implicitly[Eq[List[Int]]]

  println(List.empty[Int].toNel.filter(_.size === 1).map(_.head))

  println(List.empty[String].mkString("[", ",", "]"))

}
