package monix

import cats.Monad
import cats.implicits._
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

object TaskOptionApp extends App {

  case class Location(unit: Option[String])

  val value = for {
    l <- Task.now(Location(None)).map(_.unit)
    p <- Monad[Task].ifM(l.isDefined.pure[Task])(l.pure[Task], Task.raiseError(new Throwable("Error")))
  } yield p

  println(value.runToFuture.value)

  println(Some(1).filter(_ => true))

}
