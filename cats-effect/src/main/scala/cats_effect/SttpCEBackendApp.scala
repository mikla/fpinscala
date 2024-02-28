package cats_effect

import sttp.client4.httpclient.cats.HttpClientCatsBackend
import sttp.client4.{Response, UriContext, basicRequest}

import cats.effect.unsafe.implicits.global

import scala.concurrent.duration.{Duration, DurationInt}

object SttpCEBackendApp extends App {

  import sttp.client4.httpclient.cats.HttpClientCatsBackend

  import cats.effect.IO

  HttpClientCatsBackend.resource[IO]().use { backend =>
    basicRequest
      .get(uri"https://httpstat.us/404")
      .readTimeout(1000.millis)
      .send(backend)
      .flatMap(recordSuccessMetrics)
      .void
  }.unsafeRunSync()

  private def recordSuccessMetrics(response: Response[Either[String, String]]) =
    IO(println(s"status_${response.code.toString}"))

}
