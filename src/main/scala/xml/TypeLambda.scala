package xml

import scala.language.reflectiveCalls
import scalaz.Functor


object TypeLambda extends App {

  type lambda[R] = Either[String, R]

  val f = new Functor[lambda] {
//    override def map[A, B](fa: Either[String, A])(f: (A) => B): Either[String, B] = ???
    override def map[A, B](fa: lambda[A])(f: (A) => B): lambda[B] = ???
  }

}
