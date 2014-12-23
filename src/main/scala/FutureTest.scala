
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object FutureTest extends App {

  val f: Future[List[String]] = Future {
    List("1", "1", "2")
  }

  f onComplete {
    case Success(posts) => println(posts)
    case Failure(t) => println("Error")
  }

  Thread.sleep(10000)

}
