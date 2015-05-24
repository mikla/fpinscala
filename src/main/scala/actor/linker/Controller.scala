package actor.linker

import actor.linker.Controller.{Result, Check}
import akka.actor._
import akka.util.Timeout
import scala.concurrent.duration._

class Controller extends Actor with ActorLogging {

  context.setReceiveTimeout(10.seconds)
  context.system.scheduler.scheduleOnce(10.seconds, self, Timeout)

  var cache = Set.empty[String]
  var children = Set.empty[ActorRef]

  override def receive: Receive = {
    case Check(url, depth) =>
      log.debug("{} checking {}", depth, url)
      if (!cache(url) && depth > 0) {
        children += context.actorOf(Props(new Getter(url, depth - 1)))
      }
      cache += url
    case Getter.Done =>
      children -= sender
      if (children.isEmpty) context.parent ! Result(cache)
    case Timeout => children foreach (_ ! Getter.Abort)
  }
}

object Controller {
  case class Check(url: String, depth: Int)
  case class Result(links: Set[String])
}
