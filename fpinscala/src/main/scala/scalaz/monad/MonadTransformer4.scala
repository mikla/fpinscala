package scalaz.monad

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.StdIn

object MonadTransformer4 extends App {

  def fa(z: Int): Future[Option[Int]] = Future.successful(Some(z))
  def fb(a: Int): Future[Option[Int]] = Future.successful(Some(a))
  def fc(b: Int): Future[Option[Int]] = Future.successful(Some(b))

  class Wrapper[A](run: Future[Option[A]]) {

    def flatMap[B](f: A => Future[Option[B]]): Future[Option[B]] =
      run.flatMap {
        case Some(v) => f(v)
        case None => Future.successful(None)
      }

    def map[B](f: A => B): Future[Option[B]] = run.map {
      case Some(v) => Some(f(v))
      case _ => None
    }

  }

  def wrap[A](f: Future[Option[A]]): Wrapper[A] = new Wrapper[A](f)

  val computation = for {
    a <- wrap(fa(1))
    b <- wrap(fb(a))
    c <- wrap(fc(b))
  } yield a + b + c

  computation.onComplete(println)

  StdIn.readLine()

}
