package monix

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future

object TaskForApp extends App {

  val x = for {
    x <- Task.now("x")
    y <- Task.deferFuture(Future.failed(new Exception("fucked")))
    z <- Task.now("cool")
  } yield (x, y, z)

  x.runAsync.map(println).recover { case f => println(f) }

  readLine()

}
