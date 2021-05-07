package cats_effect.io

import cats.effect.IO
import monix.execution.atomic.AtomicBoolean

import java.io.BufferedReader
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.NonFatal

object IOApp extends App {

  val ioprint: IO[Unit] = IO {
    println("from IO ")
  }

  val program = for {
    _ <- ioprint
    _ <- ioprint
  } yield ()

  program.unsafeRunSync()

  // IO is trampolined
  def fib(n: Long, a: Long = 0, b: Long = 1): IO[Long] =
    IO(a + b).flatMap(b2 =>
      if (n > 0)
        fib(n - 1, b, b2)
      else
        IO.pure(b2))

  println(fib(500).unsafeRunSync())

  fib(500).unsafeRunAsync {
    case Left(e) => e.printStackTrace()
    case Right(_) => ()
  }

  def readLine(in: BufferedReader)(implicit ec: ExecutionContext): IO[String] =
    IO.cancelable[String] { cb =>
      val isActive = AtomicBoolean(true)
      ec.execute { () =>
        if (isActive.getAndSet(false)) {
          try cb(Right(in.readLine()))
          catch {
            case NonFatal(e) => cb(Left(e))
          }
        }
      }

      IO { // correct way to close resource
        if (isActive.getAndSet(false)) in.close()
      }
    }

  println("Make input:")
  println("read: " + readLine(Console.in).unsafeRunSync())

  // Fiber

  val cs = IO.contextShift(ExecutionContext.global)
  val launchMissiles = IO.raiseError(new Exception("bboom!"))
  val runToBunker: IO[Unit] = IO(println("To bunker"))

  val fiberIO = for {
    fiber <- launchMissiles.start(cs)
    _ <- runToBunker.handleErrorWith(error => fiber.cancel *> IO.raiseError(error))
    aftermatch <- fiber.join
  } yield aftermatch

  fiberIO.unsafeRunSync()

}
