package cats_effect

import cats.effect.concurrent.Deferred
import cats.implicits._
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

object DeferredApp extends App {

  // Deferred means values will ba obtained later and only once.
  // .get on Deferred will block until Deferred receive value
  // .get is cancellable if it's implements Concurrent

  def start(d: Deferred[Task, Int]): Task[Unit] = {
    val attemp: Int => Task[Unit] = n => d.complete(n).attempt.void

    List(
      Task.race(attemp(1), attemp(2)),
      d.get.flatMap(n => Task.now(println(show"$n")))
    ).parSequence.void
  }

  val program = for {
    d <- Deferred[Task, Int]
    _ <- start(d)
  } yield ()

  program.runSyncUnsafe()

}
