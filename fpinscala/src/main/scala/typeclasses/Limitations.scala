package typeclasses

import cats.{Functor, Traverse}

import cats.instances.list._

import scala.language.higherKinds

object Limitations extends App {

  def needFunctor[F[_] : Functor]: Unit = ()

  def tAndM[F[_] : Traverse : cats.Monad]: Unit = needFunctor[F](implicitly[Traverse[F]])


  //

  trait MonadReader[F[_], Ctx] extends Monad[F] {}
  trait MonadError[F[_], Err] extends Monad[F] {}

  def readerOnly[F[_]](implicit
    F: MonadReader[F, Int]): Unit = ()
  def errorOnly[F[_]](implicit
    F: MonadError[F, Throwable]): Unit = ()

  def rAndE[F[_]](implicit
    F0: MonadReader[F, Int],
    F1: MonadError[F, Throwable]): Unit = {
    readerOnly[F]
    errorOnly[F]
  }

//  rAndE[Monad] will fail

}
