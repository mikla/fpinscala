package catsex

import scala.concurrent.Future

import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global

object BisequenceApp extends App {

  val futureEither: Either[Future[Int], Future[String]] = Right(Future.successful("OK"))
  val result: Future[Either[Int, String]] = futureEither.bisequence

}
