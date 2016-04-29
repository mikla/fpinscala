import scala.concurrent.{ExecutionContext, Future}
import scala.xml.Elem
import ExecutionContext.Implicits.global

object FailedFutures2 extends App {

  val sibelReq = Future {
    <xml></xml>
  }.map(mapResp)

  def mapResp(elem: Elem): String = throw new RuntimeException("error")

  sibelReq.onComplete(println)

  readLine()

}
