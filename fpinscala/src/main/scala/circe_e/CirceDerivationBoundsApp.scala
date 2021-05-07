package circe_e

import io.circe.generic.extras.Configuration

object CirceDerivationBoundsApp extends App {

  trait Id[T]

  case class UserNotFound[T: Id](id: T)

  implicit val genDevConfig: Configuration = Configuration.default.withDiscriminator("$type")

//  implicit def userNotFoundEnc[T: Encoder : Id]: Encoder.AsObject[UserNotFound[T]] = deriveEncoder[UserNotFound[T]]

}
