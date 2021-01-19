package monix

import java.util.concurrent.TimeUnit

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.duration._

object TaskSleepUntilApp extends App {

  var condition = false

  global.scheduleOnce(10, TimeUnit.SECONDS, () => condition = true)

  val checkCondition = Task(condition)

  val proceed = Task.eval(println(s"Condition now: $condition. Proceeding..."))

  val p = for {
    _ <- checkCondition.restartUntil(identity)
    _ <- proceed
  } yield ()

  p.runSyncUnsafe()

}
