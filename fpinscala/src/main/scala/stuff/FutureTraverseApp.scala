package stuff

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

object FutureTraverseApp extends App {

  def runFuture(seq: Int, sleep: Int): Future[Int] = Future {
    println(s"running Future $seq, sleeping $sleep")
    Thread.sleep(sleep)
    sleep
  }

  val futures = Future.sequence {
    (0 to 20).map(i => runFuture(i, Random.nextInt(1000)))
  }

  Await.ready(futures.map(println(_)), 1.minute)

}
