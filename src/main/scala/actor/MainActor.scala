package actor

import akka.actor.{Actor, Props}

class MainActor extends Actor {

  val counter = context.actorOf(Props[Counter], "counter")

  counter ! "incr"
  counter ! "incr"
  counter ! "incr"
  counter ! "get"

  override def receive: Receive = {
    case count: Int =>
      println(count)
      context.stop(self)
  }
}
