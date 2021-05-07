package scalaz.monad

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn
import scala.language.{higherKinds, reflectiveCalls}
import scala.util.{Failure, Success}

object MonadTransformer3 extends App {

  def fa(z: Int): Future[Option[Int]] = Future.successful(Some(z))
  def fb(a: Int): Future[Option[Int]] = Future.successful(Some(a))
  def fc(b: Int): Future[Option[Int]] = Future.successful(Some(b))

  sealed trait Wrapper[A] {
    def unit(a: A): Wrapper[A]
    def flatMap[B](f: A => Wrapper[B]): Wrapper[B]
    def map[B](f: A => B): Wrapper[B]
    def run: Future[Option[A]]
  }

  case class NoneWrapper[A]() extends Wrapper[A] {
    override def unit(a: A): Wrapper[A] = NoneWrapper[A]()
    override def flatMap[B](f: (A) => Wrapper[B]): Wrapper[B] = NoneWrapper[B]()
    override def map[B](f: (A) => B): Wrapper[B] = NoneWrapper[B]()
    override def run: Future[Option[A]] = Future.successful(None)
  }

  case class SomeWrapper[A](value: A) extends Wrapper[A] {
    override def unit(a: A): Wrapper[A] = new SomeWrapper[A](a)
    override def flatMap[B](f: (A) => Wrapper[B]): Wrapper[B] = f(value)
    override def map[B](f: (A) => B): Wrapper[B] = SomeWrapper(f(value))
    override def run: Future[Option[A]] = Future.successful(Some(value))
  }

  case class FailedWrapper[A](t: Throwable) extends Wrapper[A] {
    override def unit(a: A): Wrapper[A] = FailedWrapper[A](t)
    override def flatMap[B](f: (A) => Wrapper[B]): Wrapper[B] = FailedWrapper[B](t)
    override def map[B](f: (A) => B): Wrapper[B] = FailedWrapper[B](t)
    override def run: Future[Option[A]] = Future.failed(t)
  }

  def wrap[A](f: Future[Option[A]]): Wrapper[A] =
    f.value.map {
      case Success(Some(v)) => new SomeWrapper[A](v)
      case Failure(t) => new FailedWrapper[A](t)
      case _ => new NoneWrapper[A]()
    }.getOrElse(NoneWrapper[A]())

  val computation = for {
    a <- wrap(fa(1))
    b <- wrap(fb(a))
    c <- wrap(fc(b))
  } yield a + b + c

  computation.run.onComplete(println)

  StdIn.readLine()

}
