package stuff

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

import monix.execution.Scheduler.Implicits.global
import monix.eval.Task

import scala.io.StdIn

object ConcurrentHashMapApp extends App {

  case class LocationId(id: Int) extends AnyVal

  case class KillSwitch(name: String, uuid: UUID) {
    def kill: Unit = ()
  }

  val runningProjections: ConcurrentHashMap[(LocationId, String), KillSwitch] = new ConcurrentHashMap()

  def start(p: (LocationId, String)) =
    Task.now(runningProjections.put(p, KillSwitch(p._2, UUID.randomUUID())))

  val projections = List(
    (LocationId(1), "schedule"),
    (LocationId(1), "log"),
    (LocationId(1), "location"),
    (LocationId(2), "schedule"),
    (LocationId(2), "log"),
    (LocationId(2), "location"),
    (LocationId(3), "schedule"),
    (LocationId(3), "log"),
    (LocationId(3), "location"),
  )

  Task.gatherUnordered(projections.map(start)).runToFuture

  StdIn.readLine()

  println(runningProjections)

  runningProjections.replace((LocationId(1), "log"), KillSwitch("new-log", UUID.randomUUID()))

  println(runningProjections)

}
