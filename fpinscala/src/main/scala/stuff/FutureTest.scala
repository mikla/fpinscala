package stuff

import scala.annotation.unchecked.uncheckedVariance
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn

object FutureTest extends App {

  def futureToFutureTry[T](f: Future[T]): Future[Try[T]] =
    f.map(Success(_)).recover({ case x => Failure(x) })

  def futuresToEither[T](futures: Seq[Future[T]]): Future[Either[Seq[Throwable], Seq[T]]] = {

    val calculationsResults = futures.foldLeft(Future.successful((List.empty[Throwable], List.empty[T]))) { (accF, f) =>
      f.flatMap {
        case Success(t: T @unchecked) => accF.map(acc => (acc._1, t :: acc._2))
        case Failure(e) => accF.map(acc => (e :: acc._1, acc._2))
      }
    }

    calculationsResults.map { res =>
      val (errors, values) = res
      if (errors.isEmpty) Right(values) else Left(errors)
    }
  }

  val futures = Seq(futureToFutureTry(Future(1)), futureToFutureTry(Future(2)), futureToFutureTry(Future(3)))
  futuresToEither(futures).onComplete(res => println(s"res is $res"))

  val futuresFailed = Seq(
    futureToFutureTry(Future(1)),
    futureToFutureTry(Future(2)),
    futureToFutureTry(Future(3)),
    futureToFutureTry(Future(1 / 0)),
    futureToFutureTry(Future(1 / 0)))
  futuresToEither(futuresFailed).onComplete(res => println(s"res is $res"))

  combine(
    Future(None),
    Future(Right(2)),
    Future(3)
  ).onComplete(println)

  StdIn.readLine()

  def combine(f1: Future[Option[Int]], f2: Future[Either[String, Int]], f3: Future[Int]): Future[Either[String, Int]] =
    map2(
      map2(f1, f2) {
        case (Some(x), Right(y)) => Right(x + y)
        case (None, _) => Left("empty")
        case (_, Left(l)) => Left(l)
      },
      f3) {
      case (Left(l), _) => Left(l)
      case (Right(x), y) => Right(x + y)
    }

  def map2[A, B, C](fa: Future[A], fb: Future[B])(f: (A, B) => C): Future[C] =
    for {
      a <- fa
      b <- fb
    } yield f(a, b)

}
