package catsex.free

import java.util.UUID

import cats.implicits._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.higherKinds

import cats.InjectK
import cats.data.EitherK
import cats.free.Free
import softwaremill.User

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object FreeApproachApp extends App {

  type UserAndEmailAlg[T] = EitherK[UserRepositoryAlg, EmailAlg, T]

  sealed trait UserRepositoryAlg[F]
  case class FindUser(id: UUID) extends UserRepositoryAlg[Option[User]]
  case class UpdateUser(u: User) extends UserRepositoryAlg[Unit]

  sealed trait EmailAlg[T]
  case class SendEmail(email: String, subject: String, body: String) extends EmailAlg[Unit]

  class Users[F[_]](implicit i: InjectK[UserRepositoryAlg, F]) {
    def findUser(id: UUID): Free[F, Option[User]] = Free.inject(FindUser(id))
    def updateUser(u: User): Free[F, Unit] = Free.inject(UpdateUser(u))
  }

  object Users {
    implicit def users[F[_]](implicit i: InjectK[UserRepositoryAlg, F]): Users[F] = new Users
  }

  class Emails[F[_]](implicit i: InjectK[EmailAlg, F]) {
    def sendEmail(email: String, subject: String, body: String): Free[F, Unit] =
      Free.inject(SendEmail(email, subject, body))
  }

  object Emails {
    implicit def emails[F[_]](implicit i: InjectK[EmailAlg, F]): Emails[F] =
      new Emails
  }

  def addPoints(u: UUID, points: Int)(
    implicit
    users: Users[UserAndEmailAlg],
    emails: Emails[UserAndEmailAlg]
  ): Free[UserAndEmailAlg, Either[String, Unit]] =
    users.findUser(u).flatMap {
      case None =>
        Free.pure(Left("can't update"))
      case Some(user) =>
        val updated = user.copy(loyaltyPoints = user.loyaltyPoints + points)
        for {
          _ <- users.updateUser(updated)
          _ <- emails.sendEmail(user.email, "Points added!", s"You now have ${updated.loyaltyPoints}")
        } yield Right(())
    }

  // interpretation

  import cats.~>

  val futureUserInterpreter = new (UserRepositoryAlg ~> Future) {
    override def apply[A](fa: UserRepositoryAlg[A]): Future[A] = fa match {
      case FindUser(id) =>
        /* go and talk to a database */
        Future.successful(Some(User(id, "2mail@emial.com", 10)))
      case UpdateUser(u) =>
        /* as above */
        Future.successful(())
    }
  }

  val futureEmailInterpreter = new (EmailAlg ~> Future) {
    override def apply[A](fa: EmailAlg[A]): Future[A] = fa match {
      case SendEmail(email, subject, body) =>
        /* use smtp */
        println(s"Sending email to $email")
        Future.successful(())
    }
  }

  val futureUserOrEmailInterpreter = futureUserInterpreter.or(futureEmailInterpreter)

  val res: Future[Either[String, Unit]] =
    addPoints(UUID.randomUUID(), 10).foldMap(futureUserOrEmailInterpreter)

  Await.result(res.map(println), Duration.Inf)

}
