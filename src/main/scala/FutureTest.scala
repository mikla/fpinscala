
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Try, Failure, Success}

object FutureTest extends App {

  def futureToFutureTry[T](f: Future[T]): Future[Try[T]] =
    f.map(Success(_)).recover({case x => Failure(x)})

  def futuresToEither[T](futures: Seq[Future[T]]): Future[Either[Seq[Throwable], Seq[T]]] = {

    val calculationsResults = futures.foldLeft(Future.successful((List.empty[Throwable], List.empty[T]))) { (accF, f) =>
      f flatMap {
        case Success(t: T) => accF.map(acc => (acc._1, t :: acc._2))
        case Failure(e) => accF.map(acc => (e :: acc._1, acc._2))
      }
    }

    calculationsResults.map { res =>
      val (errors, values) = res
      if (errors.isEmpty) Right(values) else Left(errors)
    }
  }

  val futures = Seq(futureToFutureTry(Future{ 1 }), futureToFutureTry(Future { 2 }), futureToFutureTry(Future { 3 }))
  futuresToEither(futures) onComplete (res => println(s"res is $res"))

  val futuresFailed = Seq(futureToFutureTry(Future{ 1 }), futureToFutureTry(Future { 2 }), futureToFutureTry(Future { 3 }), futureToFutureTry(Future { 1 / 0 }), futureToFutureTry(Future { 1 / 0 }))
  futuresToEither(futuresFailed) onComplete (res => println(s"res is $res"))

  readLine()
}
