package stuff

import cats.Functor

import scala.concurrent.ExecutionContext._
import scala.concurrent.{ExecutionContext, Future}

object FutureComposition extends App {

  class FutureFunctor(implicit ec: ExecutionContext) extends Functor[Future] {
    override def map[A, B](fa: Future[A])(f: (A) => B): Future[B] = fa.map(f)(Implicits.global)
  }

  implicit val futureFunctor = new FutureFunctor()(Implicits.global)

  implicit val optionFunctor = new Functor[Option] {
    override def map[A, B](fa: Option[A])(f: (A) => B): Option[B] = fa.map(f)
  }

  val futureOptionFunctor = futureFunctor.compose(optionFunctor)

  implicit class MapFunctorPimp[T](
    future: Future[Option[T]]
  )(implicit
    functor: Functor[({ type f[X] = Future[Option[X]] })#f]) {

    def mapF[U](f: T => U): Future[Option[U]] = functor.map(future)(f)
  }

  val ff = Future(Some(""))(Implicits.global)

}
