package io

import io.Pure.IO

object IOMonadApp extends App {

  val io = for {
    _ <- IO.point(println("IO monad"))
    _ <- IO.point(println("ALALA")).flatMap(_ => IO.point())
  } yield ()

  io.run

}

object Pure {

  sealed trait IO[A] {

    def flatMap[B](f: A => IO[B]): IO[B] =
      Suspend(() => f(this.run))

    def map[B](f: A => B): IO[B] =
      Return(() => f(this.run))

    def run: A = this match {
      case Return(f) => f()
      case Suspend(f) => f().run
    }
  }

  final case class Return[A](a: () => A) extends IO[A]
  final case class Suspend[A](f: () => IO[A]) extends IO[A]

  object IO {
    def point[A](a: => A): IO[A] = Return(() => a)
  }

}
