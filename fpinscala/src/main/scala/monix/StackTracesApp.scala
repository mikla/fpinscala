package monix

import cats.syntax.all._
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.slf4j.LoggerFactory

import scala.util.control.NonFatal

object StackTracesApp extends App {

  def logger = LoggerFactory.getLogger(this.getClass)

  println("- About to log!")
  logger.info("vikas")

  class Builder(rows: List[String]) {
    def build: Report = new Report {
      rows.map(r => Id(r.toInt))
    }
  }

  trait Err
  trait Report

  case class Id(id: Int) {
    require(id > 0, "Id must be > 0")
  }

  def serviceCall(): Task[Either[Err, List[String]]] =
    Task.now(List("1", "2", "0").asRight)

  def build() =
    serviceCall().map(_.map(entries => new Builder(entries).build))

  build().runSyncUnsafe()

  try (build().runSyncUnsafe())
  catch {
    case NonFatal(e) =>
      logger.error("failed", e)
  }

//  val x = 5
//  if (x > 4) Task.raiseError(new Exception(">")).runSyncUnsafe()

}
