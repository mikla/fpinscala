package scalafmt

import monix.execution.Scheduler

import scala.concurrent.{ExecutionContext, Future}

object ImpicitsAligning extends App {

  abstract class TagBasedProjection[E](
    driver: String
  )(implicit S: Scheduler) {

    def store()

  }

  List.empty[String].map(_ => ())

  sealed trait ActorSystem

  def dos(
    s: String
  )(implicit S: Scheduler) = ???

  def retryUntil[A](
    task: () => Future[A],
    predicate: A => Boolean,
    times: Int
  )(implicit ec: ExecutionContext,
    system: ActorSystem): Future[A] = ???

}
