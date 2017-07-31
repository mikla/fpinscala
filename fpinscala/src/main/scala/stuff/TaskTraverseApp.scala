package stuff

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Await
import scala.util.Random
import scala.concurrent.duration._

object TaskTraverseApp extends App {

  def runTask(seq: Int, sleep: Int): Task[Unit] = Task {
    println(s"Running Task $seq, sleeping $sleep")
    Thread.sleep(sleep)
    sleep
  }

  val tasks = Task.sequence { // use Task.gather to parallel things
    (0 to 20).map(i => runTask(i, Random.nextInt(1000)))
  }

  Await.ready(tasks.map(println(_)).runAsync, 1.minute)

}
