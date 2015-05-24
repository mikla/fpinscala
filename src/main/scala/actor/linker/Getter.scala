package actor.linker

import actor.linker.Getter.{Abort, Done}
import akka.actor.{Status, Actor, ActorLogging}
import akka.pattern.pipe
import org.jsoup.Jsoup

import scala.collection.JavaConverters._

class Getter(url: String, depth: Int) extends Actor with ActorLogging {

  implicit val exec = context.dispatcher

  val future = WebClient.get(url)
  future pipeTo self

  override def receive: Receive = {
    case body: String =>
      for (link <- findLinks(body))
        context.parent ! Controller.Check(link, depth)
      stop()
    case _: Status.Failure => stop()
    case Abort => stop()
  }

  // private

  private def stop(): Unit = {
    context.parent ! Done
    context.stop(self)
  }

  private def findLinks(body: String): Iterator[String] = {
    val document = Jsoup.parse(body, url)
    val links = document.select("a[href]")
    for {
      link <- links.iterator().asScala
    } yield link.absUrl("href")
  }
}

object Getter {
  case object Done
  object Abort
}
