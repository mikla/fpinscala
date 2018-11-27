package catsex

import cats.implicits._
import cats.data.EitherT
import monix.eval.Task

case class UserId(value: String) extends AnyVal
case class ServiceError(message: String) extends AnyVal
case class Address(value: String) extends AnyVal

trait UserService {
  def findUserIdByName(firstName: String, lastName: String): Task[Either[ServiceError, UserId]]
  def findUsersAddress(userId: UserId): Task[Either[ServiceError, Address]]

  def findUsersAddressByName(firstName: String, lastName: String): Task[Either[ServiceError, Address]] = {
    val result = for {
      userId <- EitherT(findUserIdByName(firstName, lastName))
      address <- EitherT(findUsersAddress(userId))
    } yield address

    result.value
  }

  {
    val x: Task[Either[ServiceError, List[UserId]]] = List(
      findUserIdByName("", ""),
      findUserIdByName("", "")
    ).sequence.map(_.sequence)
  }

}


object MonadTransformersEvoApp extends App {

}
