package catsex.effect

import cats.effect.{Async, IO}

object IOApp extends App {

  val ioprint: IO[Unit] = IO {
    println("from IO ")
  }

  val program = for {
    _ <- ioprint
    _ <- ioprint
  } yield ()

  program.unsafeRunSync()

//  val asyncIO = IO.async { cb =>
//    println("")
//  }
//
//  asyncIO.attempt

}
