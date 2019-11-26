package stuff

import cats.implicits._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

object FutureApp extends App {

//  Future.sequence(List(Future(1)))

  Left(1).left.map(_ => println("left"))

//  println(List(1).contains(_ === "string"))

  // sequence = traverse(identify)

  // F[G[X]] => G[F[X]]

  // Actors

  // property based

  // persistet actors

}
