package stuff

import java.util.UUID

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Await
import scala.util.Random
import scala.concurrent.duration._

import cats.implicits._

object TaskTraverseApp extends App {

  def runTask(seq: Int, sleep: Int): Task[Unit] = Task {
    println(s"Running Task $seq, sleeping $sleep")
    Thread.sleep(sleep)
  }

  val tasks = Task.sequence { // use Task.gather to parallel things
    (0 to 20).map(i => runTask(i, Random.nextInt(1000)))
  }

  Await.ready(tasks.map(println(_)).runToFuture, 1.minute)

  //

  case class UncoveredPosition()

  def calculate(scheduleGroup: UUID): Task[Either[Throwable, UncoveredPosition]] =
    Task.now(Right(UncoveredPosition()))

  val result: Task[Either[Throwable, List[UncoveredPosition]]] =
    Task.gatherUnordered(
      List(UUID.randomUUID())
        .map(scheduleGroup =>
          calculate(scheduleGroup)
        )
    ).map(_.sequence)

  Await.result(result.runToFuture.map(println(_)), Duration.Inf)

}
