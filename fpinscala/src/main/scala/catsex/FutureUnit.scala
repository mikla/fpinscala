package catsex

import cats.implicits._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FutureUnit extends App {

  Future(1).void

}
