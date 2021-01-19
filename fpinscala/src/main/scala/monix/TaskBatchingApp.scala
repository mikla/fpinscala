package monix

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object TaskBatchingApp extends App {

  def removeRequests(userId: Int): Task[Int] =
    Task.deferFuture(Future(println(s"Removing request for user $userId"))).map(_ => userId)

  val employeeList = 1 to 100

  val removal = Task.sequence {
    employeeList.grouped(10).map { batch =>
      //      Task.sleep(1.second).flatMap(_ => Task.gatherUnordered(batch.map(removeRequests)))
      Task.gatherUnordered(batch.map(removeRequests)).delayExecution(1.second)
    }.toList
  }

  Await.ready(removal.runToFuture, Duration.Inf)

}
