package cats_effect

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.syntax.all._

import scala.util.Try

object ParTraverseWithErrorApp extends App {

  val res = List().parTraverse(loadModel).handleError { _ =>
    println("failed")
  }

  println(res.unsafeRunSync())

  def fetchForKey(k: Int): IO[String] =
    if (k == 3) IO.raiseError(new Throwable("network error")) else IO.pure(k.toString)

  private def parseBytes(k: Int, v: String): String =
    if (k == 2) throw new Throwable("parse erro") else v

  def loadModel(k: Int): IO[Either[Throwable, (Int, String)]] =
    fetchForKey(k).map(bytes => Try(parseBytes(k, bytes)).toEither.map(m => (k, m)))
      .handleError(_.asLeft[(Int, String)])

  println(List(1,2,3).partition(_ % 2 == 0))

}
