package application

object LogbackApp extends App {

  import org.slf4j.LoggerFactory
  import org.slf4j.Logger

  val logger: Logger = LoggerFactory.getLogger("LogbackApp")

  NotificationService().notify("ex")

  logger.info("Hello, Logback!")

}
