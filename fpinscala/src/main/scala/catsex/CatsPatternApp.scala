package catsex

import cats.syntax.all._

import scala.util.{Failure, Success, Try}

object CatsPatternApp extends App {

  // 1
  // .collect { case e => e }.sum == collectFold

  val list: List[Int] = List(1, 2, 3, 4, 5)

  val yearSum = list.collect {
    case i if i % 2 == 0 => i
  }.sum

  val yearSum1 = list.collectFold {
    case i if i % 2 == 0 => i
  }

  // 2
  // .map().sum == foldMap

  val listOfStrings = List("In", "cats", "we", "trust")

  listOfStrings.map(_.length).sum

  listOfStrings.foldMap(_.length)

  // 3
  // .foldLeft(Empty)(_ |+| _) == .combineAll

  val listOfTuples = List((1, 0), (2, 2), (3, 4))

  listOfTuples.foldLeft((0, 0))(_ |+| _)

  listOfTuples.combineAll

  // 4 collectFoldSome

  val listInts = List(1, 2, 3, 5)

  listInts.flatMap(x => if (x % 2 == 0) Some(x + 1) else None).sum

  listInts.collectFoldSome(x => if (x % 2 == 0) Some(x + 1) else None)

  // 5

  Option(1) match {
    case Some(v) => Right(v)
    case None => Left("error")
  }

  Either.fromOption(Option(1), "error")

  // 6

  Try("12".toInt) match {
    case Failure(exception) => Left(exception.getMessage)
    case Success(v) => Right(v)
  }

  Either.fromTry(Try("12".toInt)).leftMap(_.getMessage)

}
