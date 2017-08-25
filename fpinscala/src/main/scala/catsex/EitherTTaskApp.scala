package catsex

import cats.data.EitherT
import monix.eval.Task
import shapelessex.application.UserId
import cats.implicits._
import monix.cats.monixToCatsMonad
import monix.execution.Scheduler.Implicits.global

case class EmployeeStats(value: Long)

object EitherTTaskApp extends App {

  def scheduleGroups(): Task[Either[Throwable, List[Long]]] = Task.now(Right(List(1, 2)))

  def getStatistics(scheduleGroup: Long): Task[Either[Throwable, Map[UserId, EmployeeStats]]] =
    Task.now(Right(Map(UserId("1") -> EmployeeStats(100))))

  def combine(): Task[Either[Throwable, Map[UserId, EmployeeStats]]] = {

    EitherT(scheduleGroups()).flatMap { groups =>
      EitherT(
        Task.gather {
          groups.map { grId =>
            getStatistics(grId)
          }
        }.map(_.sequenceU.map(_.reduce(_ ++ _)))
      )
    }.value

  }

  val tlit: List[Either[Throwable, Map[UserId, EmployeeStats]]] = List(Right(Map.empty))

  val traversed: Either[Throwable, List[Map[UserId, EmployeeStats]]] =
    tlit.sequenceU

  val traversedMap: Either[Throwable, Map[UserId, EmployeeStats]] =
    traversed.map(l => l.reduce(_ ++ _))

  combine().runAsync.onComplete(println)

  readLine()

}
