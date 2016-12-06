package patterns.magnet

import scala.concurrent.Future
import scala.language.implicitConversions
import scala.util.Try

object MagentPatternApp extends App {
  import EventMagnet._

  def applyMagnet(): EventMagnet = {
    LotteryCreated("name")
  }.apply()

}

sealed trait Event
case class LotteryCreated(name: String) extends Event

sealed trait EventMagnet {
  def apply(): Future[Event]
}

object EventMagnet {

  implicit def fromSingleEvent(event: Event): EventMagnet = new EventMagnet {
    override def apply(): Future[Event] = Future.successful(event)
  }

  implicit def fromTrySingleEvent(tryEvent: Try[Event]): EventMagnet = new EventMagnet {
    override def apply(): Future[Event] = Future.fromTry(tryEvent)
  }

  implicit def fromFuture(event: Future[Event]): EventMagnet = new EventMagnet {
    override def apply(): Future[Event] = event
  }

  implicit def fromException(ex: Exception): EventMagnet = new EventMagnet {
    override def apply(): Future[Event] = Future.failed(ex)
  }

}