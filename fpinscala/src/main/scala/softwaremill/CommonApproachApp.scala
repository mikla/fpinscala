package softwaremill

import java.util.UUID

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

case class User(id: UUID, email: String, loyaltyPoints: Int)

object CommonApproachApp extends App {

  trait EmailService {
    def sendEmail(email: String, subject: String, body: String): Future[Unit]
  }

  trait UserRepository {
    def findUser(id: UUID): Future[Option[User]]
    def updateUser(u: User): Future[Unit]
  }

  class UserRepositoryImpl extends UserRepository {
    override def findUser(id: UUID): Future[Option[User]] = Future.successful(Some(User(id, "found", 1)))
    override def updateUser(u: User): Future[Unit] = Future.successful(())
  }

  class EmailServiceImpl extends EmailService {
    override def sendEmail(email: String, subject: String, body: String): Future[Unit] = {
      println("Sending email...")
      Future.successful(())
    }
  }

  class LoyaltyPoints(ur: UserRepository,  es: EmailService) {
    def addPoints(userId: UUID, pointsToAdd: Int): Future[Either[String, Unit]] = {
      ur.findUser(userId).flatMap {
        case None => Future.successful(Left("User not found"))
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

  val userRepository = new UserRepositoryImpl
  val emailService = new EmailServiceImpl
  val loyaltyPoints = new LoyaltyPoints(userRepository, emailService)

  Await.ready(
    loyaltyPoints.addPoints(UUID.randomUUID(), 1),
    Duration.Inf)

}
