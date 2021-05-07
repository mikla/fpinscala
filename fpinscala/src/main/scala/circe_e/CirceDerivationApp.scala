package circe_e

import io.circe.derivation._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.{deriveConfiguredDecoder, deriveConfiguredEncoder}
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

object CirceDerivationApp extends App {

  sealed trait AuthError
  case class UserNotFound(id: String) extends AuthError
  case class UserIsBlocked(id: String) extends AuthError
  case object PasswordExpired extends AuthError

  implicit val genDevConfig: Configuration = Configuration.default.withDiscriminator("$type")

  implicit val userNotFoundEnc: Encoder.AsObject[UserNotFound] = deriveEncoder[UserNotFound]
  implicit val userNotFoundDec: Decoder[UserNotFound] = deriveDecoder[UserNotFound]

  implicit val unknownErrorEnc: Encoder.AsObject[UserIsBlocked] = deriveEncoder[UserIsBlocked]
  implicit val unknownErrorDec: Decoder[UserIsBlocked] = deriveDecoder[UserIsBlocked]

  implicit val passwordExpiredEnc: Encoder.AsObject[PasswordExpired.type] = deriveEncoder[PasswordExpired.type]
  implicit val passwordExpiredDec: Decoder[PasswordExpired.type] = deriveDecoder[PasswordExpired.type]

  implicit val authErrorCodec: Encoder[AuthError] = deriveConfiguredEncoder[AuthError]

  implicit val authEncoder: Decoder[AuthError] = deriveConfiguredDecoder[AuthError]


  val error1: AuthError = UserNotFound("123")
  val error2: AuthError = PasswordExpired
  val error3: AuthError = UserIsBlocked("reason")

  println(error1.asJson.noSpaces)
  println(error2.asJson.noSpaces)
  println(error3.asJson.noSpaces)

  println {
    decode[AuthError]("""{"id":"123"}""")
  }

}
