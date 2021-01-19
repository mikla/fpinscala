package monix

import monix.catnap.CircuitBreaker
import monix.eval._

import scala.concurrent.duration._
import monix.catnap.CircuitBreaker
import monix.execution.Scheduler.Implicits.{global => scheduler}

import scala.io.StdIn

object MonixExponentialBackoff extends App {

  val circuitBreaker = CircuitBreaker[Task].of(
    maxFailures = 5,
    resetTimeout = 10.seconds,
    exponentialBackoffFactor = 2,
    maxResetTimeout = 10.minutes,
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
    }
  )

  var isFailed = true

  scheduler.scheduleOnce(20.seconds) {
    isFailed = false
    println("isFailed = " + isFailed)
  }

  val problematic = Task {
    if (isFailed) throw new Exception("Failed task.")
    else 0
  }.map { v =>
    println("Launched")
    v
  }

  val protectedTask = (for {
    r <- problematic
  } yield r).onErrorRestartLoop(100.millis) { (e, delay, retry) =>
    // Exponential back-off, but with a limit
    if (delay < 60.seconds) {
      println(s"Restarting in $delay")
      retry(delay * 2).delayExecution(delay)
    } else
      Task.raiseError(e)
  }

  protectedTask.runToFuture

  StdIn.readLine()

}
