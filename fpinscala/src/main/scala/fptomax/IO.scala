package fptomax

import scala.io.StdIn

case class IO[A](unsafeRun: () => A) {self =>
  def map[B](f: A => B): IO[B] = IO(() => f(self.unsafeRun()))
  def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(self.unsafeRun()).unsafeRun())
}

object IO {
  def point[A](a: => A): IO[A] = IO(() => a)

  implicit val ProgramIO: Program[IO] = new Program[IO] {
    override def finish[A](a: => A): IO[A] = IO.point(a)
    override def chain[A, B](fa: IO[A], afb: A => IO[B]): IO[B] = fa.flatMap(afb)
    override def map[A, B](fa: IO[A], ab: A => B): IO[B] = fa.map(ab)
  }

  implicit val ConsoleIO: Console[IO] = new Console[IO] {
    override def putStrLine(line: String): IO[Unit] = IO(() => println(line))
    override def getStrLine: IO[String] = IO(() => StdIn.readLine())
  }

  implicit val RandomIO = new Random[IO] {
    override def nextInt(upper: Int): IO[Int] = IO(() => scala.util.Random.nextInt(upper))
  }
}