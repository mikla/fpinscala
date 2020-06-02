package monix

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.io.StdIn

object TaskEvalApp extends App {

  val loadSmth = for {
    _ <- Task.eval(println("starting..."))
    res <- Task(1)
    _ <- Task.eval(println("done."))
  } yield res

//  loadSmth.runAsync
//
//  readLine()
//
//  loadSmth.runAsync
//
//  readLine()

  val loadSmthNow = for {
    _ <- Task.now(println("starting now..."))
    res <- Task(1)
    _ <- Task.now(println("done now."))
  } yield res

  loadSmthNow.runToFuture

  StdIn.readLine()

  loadSmthNow.runToFuture

  StdIn.readLine()

}
