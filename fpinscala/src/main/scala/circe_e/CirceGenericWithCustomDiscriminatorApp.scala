package circe_e

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json}
import util.reflection.safeNameTrimmed

object CirceGenericWithCustomDiscriminatorApp extends App {

  val Discriminator = "$type"

  sealed trait AuthError

  case class UserNotFound(userId: String) extends AuthError
  case class UserIsBlocked(userId: String) extends AuthError
  case object PasswordExpired extends AuthError

  implicit val userNotFoundEncoder: Encoder[UserNotFound] = deriveEncoder[UserNotFound]
  implicit val userIsBlockedEncoder: Encoder[UserIsBlocked] = deriveEncoder[UserIsBlocked]
  implicit val passwordExpiredEncoder: Encoder[PasswordExpired.type] = deriveEncoder[PasswordExpired.type]

  implicit val userNotFoundDecoder: Decoder[UserNotFound] = deriveDecoder[UserNotFound]
  implicit val userIsBlockedDecoder: Decoder[UserIsBlocked] = deriveDecoder[UserIsBlocked]
  implicit val passwordExpiredDecoder: Decoder[PasswordExpired.type] = deriveDecoder[PasswordExpired.type]

  def asJsonWithDiscriminator[A : Encoder](a: A): Json =
    a.asJson.asObject.map(_.add(Discriminator, Json.fromString(safeNameTrimmed(a))).asJson).get

  implicit val authErrorEncoder: Encoder[AuthError] = Encoder.instance {
    case a: UserNotFound => asJsonWithDiscriminator(a)
    case a: UserIsBlocked => asJsonWithDiscriminator(a)
    case a: PasswordExpired.type => asJsonWithDiscriminator(a)
  }

  implicit val authErrorDecoder: Decoder[AuthError] = Decoder.instance(c =>
    c.downField(Discriminator).as[String].flatMap {
      case "UserNotFound" => c.as[UserNotFound]
      case "UserIsBlocked" => c.as[UserIsBlocked]
      case "PasswordExpired" => c.as[PasswordExpired.type]
    })

  val error: AuthError = UserNotFound("123")

  val userNotFoundJson = error.asJson.toString()

  println(userNotFoundJson)

  println(decode[AuthError](userNotFoundJson))

}
