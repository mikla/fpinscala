package monix

import cats.implicits._
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object TraverseOrderApp extends App {

  val f = List(1, 2, 3, 4, 5)
    .traverse(v => Future(println(s"Executing $v")))

  println(
    Await.result(f, Duration.Inf)
  )

}
