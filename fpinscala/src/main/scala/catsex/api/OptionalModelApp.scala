package catsex.api

import cats.Id
import cats.implicits._

import java.time.LocalDateTime

object OptionalModelApp extends App {

  case class UserModel[F[_]](
    userId: F[Long],
    name: String,
    createdAt: LocalDateTime)

  val createModel = UserModel[Option](none, "New User", LocalDateTime.now())

  val updateModel = UserModel[Id](1, "New User 1", LocalDateTime.now())

}
