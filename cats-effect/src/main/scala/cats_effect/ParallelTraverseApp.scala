package cats_effect

import cats.{MonadThrow, Parallel}
import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Sync}
import cats.syntax.all._

object ParallelTraverseApp extends App {

  class Nofiyer[F[_] : Sync : Parallel] {

    var nofied = List.empty[String]
    var error = List.empty[String]

    def call(url: String, index: Int): F[String] =
      if (index % 2 == 0) {
        Sync[F].pure(s"Ok for $url")
      } else {
        Sync[F].raiseError[String](new NotImplementedError())
      }

    def notifyLose(urls: List[String]): F[List[String]] =
      urls.zipWithIndex.parTraverse {
        case (url, index) =>
          call(url, index).recoverWith {
            case t =>
              s"Notify failed for $url".pure[F]
          }
      }

  }

  val service = new Nofiyer[IO]

  val x = service.notifyLose(List("url1", "url2", "url3")).unsafeRunSync()

  println(x)

  def put[F[_] : MonadThrow] = MonadThrow[F].pure(println("putting to cache"))
  val bid: Option[String] = None
  def inc[F[_] : MonadThrow] = MonadThrow[F].pure(println("counter increased"))

  def cacheBid[F[_] : MonadThrow]() =
    put >>
      bid.traverse(bid => MonadThrow[F].pure(println(s"sending to kafka: $bid"))) >>
      inc

  cacheBid[IO].unsafeRunSync()

}
