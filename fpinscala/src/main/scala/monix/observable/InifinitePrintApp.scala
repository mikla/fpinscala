package monix.observable

import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object InifinitePrintApp extends App {

  Await.result(
    Observable.repeat(false, true).foreach(e => if (e) println("First") else println("Second")),
    Duration.Inf
  )

  while (true) {}

}
