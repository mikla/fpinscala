package tf

import cats.{Applicative, Id, Monad}
import model.Employee
import monix.eval.Task
import cats.implicits._
import monix.execution.Scheduler.Implicits.global
import typeclasses.Limitations.MonadError

object TFAppEx extends App {

  case class Request(employeeName: String)

  class UsersService[F[_] : Applicative] {
    def getUsers(location: String): F[List[Employee]] = Applicative[F].pure(Nil)
  }

  class RequestsService[F[_] : Applicative] {
    def getRequests(employees: List[Employee]): F[List[Request]] = Applicative[F].pure(Nil)
  }

  class Controller[F[_] : Monad](
    usersService: UsersService[F],
    requestsService: RequestsService[F]
  ) {

    def getResults: F[List[Request]] = for {
      users <- usersService.getUsers("riga")
      req <- requestsService.getRequests(users)
    } yield req

  }

  // Test

  val testContoller = new Controller[Id](
    new UsersService[Id],
    new RequestsService[Id])

  testContoller.getResults

  // Real app

  val ctl = new Controller[Task](
    new UsersService[Task],
    new RequestsService[Task])

  ctl.getResults.runToFuture

}
