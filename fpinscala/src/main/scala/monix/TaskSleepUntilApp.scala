package monix

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import java.util.concurrent.TimeUnit

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
