package monix

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object MonixMapToEitherApp extends App {

  val task: Task[Int] = Task(1)

  trait Error

  val res1: Task[Right[Nothing, Int]] = task.map(Right(_))
  val res2: Task[Either[Error, Int]] = task.map(Right(_))

  println(Await.result(res1.runToFuture, Duration.Inf))
  println(Await.result(res2.runToFuture, Duration.Inf))

  val taskFailed: Task[Int] = Task.deferFuture(Future.failed(new Exception("failed")))

  val res1failed = taskFailed.map(Right(_)).onErrorHandle(e => println(e.getMessage))

  val res2failed: Task[Either[Error, Int]] = taskFailed.map(Right(_))

  println(Await.result(res1failed.runToFuture, Duration.Inf))
//  println(Await.result(res2failed.runToFuture, Duration.Inf))

}
