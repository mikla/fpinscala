package catsex.api

import java.time.LocalDateTime

import cats.{Functor, Id}
import cats.implicits._
import cats.derived._

object OptionalModelApp extends App {

  case class UserModel[F[_]](
    userId: F[Long],
    name: String,
    createdAt: LocalDateTime)

  val createModel = UserModel[Option](none, "New User", LocalDateTime.now())

  val updateModel = UserModel[Id](1, "New User 1", LocalDateTime.now())

}
