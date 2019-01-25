package monix

import cats.implicits._
import cats.effect.{Async, Timer}
import monix.catnap.CircuitBreaker
import monix.eval.Task
import monix.execution.Scheduler.Implicits.{global => scheduler}
import monix.execution.exceptions.ExecutionRejectedException

import scala.concurrent.duration._

object TaskRetryApp extends App {

  var isFailed = true

  scheduler.scheduleOnce(20.seconds) {
    isFailed = false
    println("isFailed = " + isFailed)
  }

  val task = Task {
    if (isFailed) throw new Exception("Failed task.")
    else 0
  }.map { v =>
    println("Launched")
    v
  }

  val circuitBreaker = CircuitBreaker[Task].of(
    maxFailures = 1,
    resetTimeout = 2.seconds,
    maxResetTimeout = 5.seconds,

    onRejected = Task {
      println("Task rejected in Open or HalfOpen")
    },
    onClosed = Task {
      println("Switched to Close, accepting tasks again")
    },
    onHalfOpen = Task {
      println("Switched to HalfOpen, accepted one task for testing")
    },
    onOpen = Task {
      println("Switched to Open, all incoming tasks rejected for the next 10 seconds")
    },
    exponentialBackoffFactor = 2,
  )

  //  (for {
  //    cb <- circuitBreaker
  //    t <- cb.protect(task)
  //  } yield t).onErrorRestartLoop(0) { (e, times, retry) =>
  //    println("retry")
  //    circuitBreaker.flatMap(_.awaitClose.flatMap { _ =>
  //      println("try")
  //      retry(times + 1)
  //    })
  //  }.runSyncUnsafe()

//  (for {
//    cb <- circuitBreaker
//    t <- protectWithRetry2(task, cb)
//  } yield t).runSyncUnsafe()

//      protectWithRetry2(task, circuitBreaker).attempt.runToFuture.onComplete {
//        case scala.util.Failure(exception) => println(exception)
//        case scala.util.Success(value) => println(value)
//      }

  def protectWithRetry2[F[_], A](task: F[A], cb: CircuitBreaker[F])
    (implicit F: Async[F]): F[A] = {

    cb.protect(task).recoverWith {
      case _: ExecutionRejectedException =>
        println("-")
        // Waiting for the CircuitBreaker to close, then retry
        cb.awaitClose.flatMap { _ =>
          println("try")
          protectWithRetry2(task, cb)
        }
    }
  }

  def protectWithRetry[F[_], A](task: F[A], cb: CircuitBreaker[F], delay: FiniteDuration)
    (implicit F: Async[F], timer: Timer[F]): F[A] = {

    cb.protect(task).recoverWith {
      case _: ExecutionRejectedException =>
        // Sleep, then retry
        println(delay)
        timer.sleep(delay).flatMap(_ => protectWithRetry(task, cb, delay * 2))
    }
  }

  //  (for {
  //    cb <- circuitBreaker
  //    t <- protectWithRetry(task, cb, 2.seconds)
  //  } yield t).runToFuture

  readLine()

}
