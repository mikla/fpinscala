package cats_effect.galvan

import cats.data.ValidatedNec
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.catsSyntaxApplicativeId

object Controller {

  case class Request(fromAccount: String, toAccount: String, amount: String)

  case class Response(status: Int, body: String)

  def postTransfer(request: Request): IO[Response] = ???

}

object ErrorHandlingApp extends IOApp {

  import Controller._

  override def run(args: List[String]): IO[ExitCode] = {
    val r = Request("12345", "5678", "2000")
    postTransfer(r).flatTap(println(_).pure[IO]).as(ExitCode.Success)
  }
}

object Validations {

  type Valid[A] = ValidatedNec[String, A]

}
