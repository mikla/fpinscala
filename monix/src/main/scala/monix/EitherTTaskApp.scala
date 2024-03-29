package monix

import cats.data.EitherT
import cats.implicits._
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.io.StdIn

case class EmployeeStats(value: Long)

object EitherTTaskApp extends App {

  def scheduleGroups(): Task[Either[Throwable, List[Long]]] = Task.now(Right(List(1, 2)))

  def getStatistics(scheduleGroup: Long): Task[Either[Throwable, Map[UserId, EmployeeStats]]] =
    Task.now(Right(Map(UserId("1") -> EmployeeStats(100))))

  def combine(): Task[Either[Throwable, Map[UserId, EmployeeStats]]] =
    EitherT(scheduleGroups()).flatMap { groups =>
      EitherT(
        Task.parSequence {
          groups.map(grId => getStatistics(grId))
        }.map(_.sequence.map(_.reduce(_ ++ _)))
      )
    }.value

  val tlit: List[Either[Throwable, Map[UserId, EmployeeStats]]] = List(Right(Map.empty))

  val traversed: Either[Throwable, List[Map[UserId, EmployeeStats]]] =
    tlit.sequence

  val traversedMap: Either[Throwable, Map[UserId, EmployeeStats]] =
    traversed.map(l => l.reduce(_ ++ _))

  combine().runToFuture.onComplete(println)

  StdIn.readLine()

  case class UserId(str: String)
}
