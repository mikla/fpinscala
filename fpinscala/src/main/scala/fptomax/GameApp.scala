package fptomax

import scala.util.Try
import Program._
import Console._
import Random._

object GameApp extends App {

  def parseInt(s: String): Option[Int] = Try(s.toInt).toOption

  def checkAnswer[F[_] : Console](name: String, num: Int, guess: Int): F[Unit] =
    if (num == guess) Console.putStrLine("You guessed right, " + name + "!")
    else Console.putStrLine("You guessed wrong, " + name + "! The number was: " + num)

  def printResults[F[_] : Console](input: String, num: Int, name: String): F[Unit] =
    parseInt(input).fold(
      Console.putStrLine("You did not enter a number")
    )(guess =>
      if (guess == num) Console.putStrLine("You guessed right, " + name + "!")
      else Console.putStrLine("You guessed wrong, " + name + "! The number was: " + num)
    )

  def checkContinue[F[_] : Program : Console](name: String): F[Boolean] =
    for {
      _ <- Console.putStrLine("Do you want to continue, " + name + "?")
      choice <- Console.getStrLine.map(_.toLowerCase)
      cont <- if (choice == "y") finish(true)
      else if (choice == "n") finish(false)
      else checkContinue(name)
    } yield cont

  def gameLoop[F[_] : Program : Console : Random](name: String): F[Unit] =
    for {
      num <- Random.nextInt(5).map(_ + 1)
      _ <- Console.putStrLine("Dear " + name + ", please guess a number from 1 to 5:")
      guess <- Console.getStrLine
      _ <- parseInt(guess).fold(
        Console.putStrLine("That is not a valid selection, " + name + "!")
      )((guess: Int) => checkAnswer(name, num, guess))
      cont <- checkContinue(name)
      _ <- if (cont) gameLoop(name) else finish(())
    } yield ()

  def main[F[_] : Program : Random : Console]: F[Unit] =
    for {
      _ <- Console.putStrLine("What is your name?")
      name <- getStrLine
      _ <- Console.putStrLine("Hello, " + name + ", welcome.")
      _ <- gameLoop(name)
    } yield ()

  def mainIO: IO[Unit] = main[IO]
  def mainTestIO: TestIO[Unit] = main[TestIO]

  // Testing

  val ExampleData = TestData(
    input = "John" :: "1" :: "n" :: Nil,
    output = Nil,
    nums = 0 :: Nil
  )

  def runTest = mainTestIO.eval(ExampleData).showResults

  println(runTest)

}
