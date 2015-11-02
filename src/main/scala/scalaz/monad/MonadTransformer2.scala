package scalaz.monad

import scala.concurrent.ExecutionContext
import scalaz._
import scalaz.OptionT._
import scalaz.Scalaz._
import ExecutionContext.Implicits.global

object MonadTransformer2 extends App {

  import scala.concurrent.Future

  def fa(z: Int): Future[Option[Int]] = Future.successful(Some(z))
  def fb(a: Int): Future[Option[Int]] = Future.failed(new RuntimeException("fsd"))
  def fc(b: Int): Future[Option[Int]] = Future.successful(Some(b))

  // Scalaz

  def fz(z: Int) =
    for {
      a <- optionT(fa(z))
      b <- optionT(fb(a))
      c <- optionT(fc(b))
    } yield a + b + c
  fz(1).run onComplete println

  // Plain Scala

  def optT[A](f: Future[Option[A]]): Future[A] = f.map {
    case Some(a) => a
    case _ => throw new RuntimeException("optT empty")
  }

  def fz2(z: Int): Future[Option[Int]] = {
    val computation = for {
      a <- optT(fa(z))
      b <- optT(fb(a))
      c <- optT(fc(b))
    } yield a + b + c
    computation.map(Option(_)).recover { case _ => None }
  }
  fz2(1) onComplete println

  // 2

  def fz3(z: Int): Future[Option[Int]] = {
    (for {
      aOpt <- fa(z)
      bOpt <- fb(aOpt.getOrElse(throw new RuntimeException))
      cOpt <- fc(bOpt.getOrElse(throw new RuntimeException))
    } yield (aOpt, bOpt, cOpt)) map {
      case (Some(a), Some(b), Some(c)) => Some(a + b + c)
      case _ => None
    } recover { case _ => None}
  }
  fz3(1) onComplete println

  readLine()

}
