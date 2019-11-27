package tf

import java.util.UUID

import cats.implicits._
import cats.Monad

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import cats.Monad
import softwaremill.User

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object TaglessFinalApproachApp extends App {

  trait UserRepositoryAlg[F[_]] {
    def findUser(id: UUID): F[Option[User]]
    def updateUser(u: User): F[Unit]
  }

  trait EmailAlg[F[_]] {
    def sendEmail(email: String, subject: String, body: String): F[Unit]
  }

  class LoyaltyPoints[F[_] : Monad](ur: UserRepositoryAlg[F], es: EmailAlg[F]) {
    def addPoints(userId: UUID, pointsToAdd: Int): F[Either[String, Unit]] = {
      ur.findUser(userId).flatMap {
        case None => implicitly[Monad[F]].pure(Left("User not found"))
        case Some(user) =>
          val updated = user.copy(loyaltyPoints = user.loyaltyPoints + pointsToAdd)
          for {
            _ <- ur.updateUser(updated)
            _ <- es.sendEmail(user.email, "Points added!",
              s"You now have ${updated.loyaltyPoints}")
          } yield Right(())
      }
    }
  }

  trait FutureInterpreter extends UserRepositoryAlg[Future] {
    override def findUser(id: UUID): Future[Option[User]] =
      Future.successful(Some(User(id, "email.", 1)))

    override def updateUser(u: User): Future[Unit] =
      Future.successful(())
  }

  trait FutureEmailInterpreter extends EmailAlg[Future] {
    override def sendEmail(email: String, subject: String,
      body: String): Future[Unit] = {
      println(s"sending email $email")
      Future.successful(())
    }
  }

  val result: Future[Either[String, Unit]] =
    new LoyaltyPoints(new FutureInterpreter {}, new FutureEmailInterpreter {}).addPoints(UUID.randomUUID(), 10)

  Await.result(result, Duration.Inf)

}
