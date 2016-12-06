package free.softwaremill

import cats.Monad

import scala.concurrent.Future
import scala.language.higherKinds

trait Free[S[_], A]
case class Pure[S[_], A](value: A) extends Free[S, A]
case class FlatMap[S[_], A, B](p: Free[S, A], f: A => Free[S, B]) extends Free[S, B]
case class Suspend[S[_], A](s: S[A]) extends Free[S, A]

/*
trait TicketingService {
  def invoke(count: Int): Tickets
}

trait TicketingServoce {
  def invoke(count: Int): Future[Tickets]
}
*/



trait TicketingService[M[_]] {
  implicit def m: Monad[M]
  def invoke(count: Int): M[Tickets]
}



