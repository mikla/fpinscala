package monix

import cats.implicits._
import cats.effect.concurrent.MVar
import monix.eval.Task
import monix.execution.Scheduler
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Await
import scala.concurrent.duration._

object MVarApp extends App {

  val io = Scheduler.io()

  val lock: Task[MVar[Task, Unit]] = MVar[Task].empty.memoize

  val task1 = for {
    l <- lock
    _ <- Task(println("Task 1")).delayExecution(2.seconds)
    _ <- l.put(())
  } yield ()

  val task2 = for {
    l <- lock
    _ <- l.read
    _ <- Task(println("Task 2"))
  } yield ()

  task2.executeOn(io).runToFuture

  Thread.sleep(1000)

  task1.executeOn(io).runToFuture


  readLine()

  //  Await.result((otherCommands, importLocations).mapN((_, _) => 0).runToFuture, Duration.Inf)
}
