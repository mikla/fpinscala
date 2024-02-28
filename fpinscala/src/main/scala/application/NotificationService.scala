package application

import org.slf4j.{Logger, LoggerFactory}

trait NotificationService {

  def notify(message: String): Unit

}

object NotificationService {

  val logger: Logger = LoggerFactory.getLogger("application.NotificationService")

  def apply(): NotificationService = (message: String) =>
    try if (message == "ex") throw new RuntimeException("ex")
    else println(s"Notification: $message")
    catch {
      case e: Exception => logger.error("Error in NotificationService", e)
    }

}
