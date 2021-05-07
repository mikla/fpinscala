package catsex

import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object FutureUnit extends App {

  Future(1).void

}
