import java.util.concurrent.ConcurrentLinkedQueue

import org.scalatest.{MustMatchers, WordSpec}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

class ManualECSpec extends WordSpec with MustMatchers {

  class ManualEC extends ConcurrentLinkedQueue[Runnable] with ExecutionContext {

    override def execute(runnable: Runnable): Unit = this add runnable

    override def reportFailure(cause: Throwable): Unit = cause.printStackTrace(System.err)

    def interpNext(): Boolean =
      Option(poll()).fold(false) { r =>
        println(s"Interpreting $r next")
        r.run()
        !isEmpty
      }

  }

  "ManualEC" should {
    "require manual EC" in {
      implicit val ec = new ManualEC
      val f = Future(List("make", "foo", "bar").mkString(" "))
        .map(identity)

      ec.interpNext()
      ec.interpNext()

      Await.result(f, 1.second) must equal ("make foo bar")
    }
  }


}
