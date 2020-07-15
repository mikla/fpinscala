package monix

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MonixGatherFailedApp extends App {

  val list = List(Task(1), Task(throw new Exception("faiiled")).onErrorRecover {
    case _ => 2
  })

  println(
    Await.result(
      Task.gatherUnordered(list).map(_.sum).runToFuture,
      Duration.Inf
    )
  )

}
