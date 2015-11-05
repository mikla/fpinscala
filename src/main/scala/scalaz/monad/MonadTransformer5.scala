package scalaz.monad

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MonadTransformer5 extends App {

  def fa(z: Int): Future[Option[Int]] = Future.successful(Some(z))
  def fb(a: Int): Future[Option[Int]] = Future.successful(Some(a))
  def fc(b: Int): Future[Option[Int]] = Future.successful(Some(b))

  case class Wrapper[A](var run: Future[Option[A]]) {
    def unit[B](a: B): Wrapper[B] = Wrapper(Future.successful(Some(a)))

    def flatMap[B](f: A => Wrapper[B]): Wrapper[B] = wrap {
      run.flatMap {
        case Some(v) => f(v).run
        case _ => Future.successful(None)
      }
    }

    def map[B](f: A => B): Wrapper[B] = this.flatMap(a => unit(f(a)))

  }

  def wrap[A](f: Future[Option[A]]): Wrapper[A] = new Wrapper[A](f)

  val computation = for {
    a <- wrap(fa(1))
    b <- wrap(fb(a))
    c <- wrap(fc(b))
  } yield a + b + c

  computation.run onComplete println

  readLine()

}
