package cats_effect

import cats.Id
import cats.effect.{ExitCase, IO, Sync}
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import monix.eval.Coeval

object SyncApp extends App {

  val result = for {
    logger <- Slf4jLogger.create[Coeval]
    _ <- logger.debug("message")
  } yield ()

  result.attempt.value

}
