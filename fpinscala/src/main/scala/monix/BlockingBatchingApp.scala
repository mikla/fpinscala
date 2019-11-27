package monix

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object BlockingBatchingApp extends App {

  val tasksToExecute = (1 to 1000).map { i =>
    Task {
      println(s"Executing ${i} task")
      Thread.sleep(5000 + i)
      println(s"Task ${i} done")
    }
  }


  Await.result(Task.sequence(tasksToExecute).runToFuture, Duration.Inf)

}
