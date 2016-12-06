package scalaz.monad

import scala.concurrent.Future
import scalaz._
import Scalaz._
import scalaz.OptionT.optionT

object MonadTransformers extends App {
  type Result[+A] = String \/ Option[A]

  var result: Result[Int] = some(42).right

  val transformed = for {
    option <- result
  } yield {
      for {
        value <- option
      } yield value.toString
    }

  println(transformed)
  val transformed2 = result map {
    _ map {
      _.toString
    }
  }
  println(transformed2)

  // ---

  type Error[+A] = \/[String, A]
  type ResultM[A] = OptionT[Error, A]

  val resultM: ResultM[Int] = 42.point[ResultM]
  val transformedM = result map (_.toString)
  println(transformedM)

  println(None.point[ResultM] map (_.toString))
  println(OptionT(none[Int].point[Error]))

  // ---

  /**
  def getGrantToken: Future[Option[String]] = Future.successful("grant-token".some)
  def getSession: Future[Option[String]] = Future.successful("session".some)

  val x = for {
    token <- optionT(getGrantToken)
    session <- optionT(getSession)
  } yield token :: session :: HNil

  val y = x.run map (_.getOrElse(throw new Exception))
  */

}
