package catsex.exercises

import java.util.UUID

import cats.data.Validated
import cats.instances.list._
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.io.StdIn

object ValidatedApp extends App {

  sealed trait CommandProcessingResult
  case class CommandOk(id: UUID) extends CommandProcessingResult
  case class CommandFailed(id: UUID, reason: String) extends CommandProcessingResult

  def process(): Task[List[CommandProcessingResult]] =
    Task.now(
      List(
        CommandOk(UUID.randomUUID()),
        CommandOk(UUID.randomUUID())
//        CommandFailed(UUID.randomUUID(), "Bad"),
//        CommandFailed(UUID.randomUUID(), "Bad2")
      )
    )

  def validateCommandProcessingResult(
    res: CommandProcessingResult): Validated[List[String], List[UUID]] = res match {
    case CommandOk(id) => Validated.valid(List(id))
    case CommandFailed(_, reason) => Validated.invalid(List(reason))
  }

  process().map(commandsRes =>
    commandsRes.map(validateCommandProcessingResult).reduce(_ combine _)
  ).runToFuture.onComplete(r => println(r))

  StdIn.readLine()

}
