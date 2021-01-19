package jdg.functionalscala.ziorea

import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

import jdg.functionalscala.ziorea.Hangman.GuessResult.{Correct, Incorrect, Lost, Unchanged, Won}
import zio._

import scala.io.Source

object HelloWorld extends App {

  import zio.console._

  /**
    * EXERCISE 1
    *
    * Implement a simple "Hello World" program using the effect returned by `putStrLn`.
    */
  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    putStrLn("Hello, World").as(ExitCode.success)

}

object ErrorConversion extends App {
  val StdInputFailed = 1

  import zio.console._

  val failed: ZIO[Console, String, Unit] =
    putStrLn("About to fail...") *>
      ZIO.fail("Uh oh!") *>
      putStrLn("This will NEVER be printed!")

  /**
    * EXERCISE 2
    *
    * Using `ZIO#orElse` or `ZIO#fold`, have the `run` function compose the
    * preceding `failed` effect into the effect that `run` returns.
    */
  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    (failed.as(ExitCode.success)).orElse(ZIO.succeed(ExitCode.failure))
}

object PromptName extends App {
  val StdInputFailed = 1

  import zio.console._

  /**
    * EXERCISE 3
    *
    * Implement a simple program that asks the user for their name (using
    * `getStrLn`), and then prints it out to the user (using `putStrLn`).
    */
  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    putStrLn("Name?") *>
      getStrLn.flatMap(name => putStrLn(s"Good, " + name)).fold(_ => ExitCode.failure, _ => ExitCode.success)
}

object ZIOTypes {
  type ??? = Nothing

  // ZIO[Any, _, _] meaning we don't need env

  def compose[R, S, E, A, B](left: ZIO[R, E, A], right: ZIO[S, E, B]): ZIO[R with S, E, (A, B)] =
    ???

  /**
    * EXERCISE 4
    *
    * Provide definitions for the ZIO type aliases below.
    */
  type Task[+A] = ZIO[Any, Throwable, A]
  type UIO[+A] = ZIO[Any, Nothing, A]
  type RIO[-R, +A] = ZIO[R, Throwable, A]
  type IO[+E, +A] = ZIO[Any, E, A]
  type URIO[-R, +A] = ZIO[R, Nothing, A]
}

object NumberGuesser extends App {

  import zio.console._
  import zio.random._

  def analyzeAnswer(random: Int, guess: String): ZIO[Console, Nothing, Unit] =
    if (random.toString == guess.trim) putStrLn("You guessed correctly!")
    else putStrLn(s"You did not guess correctly. The answer was $random")

  /**
    * EXERCISE 5
    *
    * Choose a random number (using `nextInt`), and then ask the user to guess
    * the number, feeding their response to `analyzeAnswer`, above.
    */
  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    (for {
      _ <- putStrLn("Welcome")
      random <- nextIntBounded(3)
      _ <- putStrLn("Enter a guess from 0 to 3")
      guess <- getStrLn
      _ <- analyzeAnswer(random, guess)
    } yield (ExitCode.success)).orElse(ZIO.succeed(ExitCode.failure))
}

object AlarmApp extends App {

  import zio.console._
  import zio.duration._
  import java.io.IOException

  def parseDouble(line: String): ZIO[Any, NumberFormatException, Double] =
    ZIO.effect(line.toDouble).refineToOrDie[NumberFormatException]

  /**
    * EXERCISE 6
    *
    * Create an effect that will get a `Duration` from the user, by prompting
    * the user to enter a decimal number of seconds.
    */
  lazy val getAlarmDuration: ZIO[Console, IOException, Duration] = {
    def parseDuration(input: String): IO[NumberFormatException, Duration] =
      parseDouble(input).map(double => Duration(double.toLong, TimeUnit.SECONDS))

    def fallback(input: String): ZIO[Console, IOException, Duration] =
      for {
        duration <- parseDuration(input).orElse(
          putStrLn("invalid input") *> getAlarmDuration
        )
      } yield duration

    getStrLn.flatMap(fallback)
  }

  /**
    * EXERCISE 7
    *
    * Create a program that asks the user for a number of seconds to sleep,
    * sleeps the specified number of seconds, and then prints out a wakeup
    * alarm message.
    */
  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    (for {
      _ <- putStrLn("Enter")
      duration <- getAlarmDuration
      _ <- ZIO.sleep(duration)
      _ <- putStrLn("Wake up")
    } yield (ExitCode.success)).orElse(ZIO.succeed(ExitCode.failure))

  val died1: UIO[Unit] =
    ZIO.unit
      .map(_ => throw new Error("Sneaky error"))
      .ensuring(UIO {
        throw new Error("Finalizer Error")
      })

}

object Cat extends App {

  import zio.console._
  import zio.blocking._
  import java.io.IOException

  /**
    * EXERCISE 8
    *
    * Implement a function to read a file on the blocking thread pool, storing
    * the result into a string.
    */
  def readFile(file: String): ZIO[Blocking, IOException, String] =
    effectBlocking {
      Source.fromFile(file).getLines().mkString("\n")
    }.refineToOrDie[IOException]

  /**
    * EXERCISE 9
    *
    * Implement a version of the command-line utility "cat", which dumps the
    * contents of the specified file to standard output.
    */

  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    args match {
//      case file :: Nil => (readFile(file) >>= putStrLn).fold(_ => ExitCode.failure, _ => ExitCode.success)
      case _ => putStrLn("Usafe: cat <file>").as(ExitCode.failure)
    }
}

object CatIncremental extends App {

  import zio.console._
  import zio.blocking._
  import java.io._

  /**
    * EXERCISE 10
    *
    * Implement all missing methods of `FileHandle`. Be sure to do all work on
    * the blocking thread pool.
    */
  final case class FileHandle private (private val is: InputStream) {
    final def close: ZIO[Blocking, IOException, Unit] =
      effectBlocking(is.close()).refineToOrDie[IOException]

    final def read: ZIO[Blocking, IOException, Option[Chunk[Byte]]] =
      effectBlocking {
        val array = Array.ofDim[Byte](1024)
        val read = is.read(array)
        if (read != -1) Some(Chunk.fromArray(array).take(read))
        else None
      }.refineToOrDie[IOException]
  }

  object FileHandle {
    final def open(file: String): ZIO[Blocking, IOException, FileHandle] =
      effectBlocking(new FileHandle(new FileInputStream(file))).refineToOrDie[IOException]
  }

  /**
    * EXERCISE 11
    *
    * Implement an incremental version of the `cat` utility, using `ZIO#bracket`
    * or `ZManaged` to ensure the file is closed in the event of error or
    * interruption.
    */
  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    args match {
      case file :: Nil =>
        def cat(fh: FileHandle): ZIO[Blocking with console.Console, IOException, Unit] =
          fh.read.flatMap {
            case None => ZIO.unit
            case Some(chunk) =>
              putStrLn(new String(chunk.toArray, StandardCharsets.UTF_8)) *> cat(fh)
          }

        (for {
          handle <- FileHandle.open(file)
          _ <- cat(handle)
          _ <- handle.close
        } yield 0).orElse(ZIO.succeed(1))

        // or using bracket

        FileHandle.open(file).bracket(_.close.ignore)(cat).fold(_ => ExitCode.failure, _ => ExitCode.success)
    }
}

object ComputePi extends App {

  import zio.random._
  import zio.console._
  import zio.duration._

  /**
    * Some state to keep track of all points inside a circle,
    * and total number of points.
    */
  final case class PiState(inside: Ref[Long], total: Ref[Long])

  /**
    * A function to estimate pi.
    */
  def estimatePi(inside: Long, total: Long): Double =
    (inside.toDouble / total.toDouble) * 4.0

  /**
    * A helper function that determines if a point lies in
    * a circle of 1 radius.
    */
  def insideCircle(x: Double, y: Double): Boolean =
    Math.sqrt(x * x + y * y) <= 1.0

  /**
    * An effect that computes a random (x, y) point.
    */
  val randomPoint: ZIO[Random, Nothing, (Double, Double)] =
    nextDouble.zip(nextDouble)

  /**
    * EXERCISE 12
    *
    * Build a multi-fiber program that estimates the value of `pi`. Print out
    * ongoing estimates continuously until the estimation is complete.
    */
  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] = {
    def computePi(piState: PiState): ZIO[Random, Nothing, Nothing] = (for {
      point <- randomPoint
      (x, y) = point
      _ <- piState.total.update(_ + 1)
      _ <- piState.inside.update(count => if (insideCircle(x, y)) count + 1 else count)
    } yield ()).forever

    def reportStatus(piState: PiState) =
      (for {
        total <- piState.total.get
        inside <- piState.inside.get
        estimate = estimatePi(inside, total)
        _ <- putStrLn(s"$estimate")
        _ <- ZIO.sleep(1.second)
      } yield ()).forever

    // Single worker version
    for {
      piState <- (Ref.make(0L).zipWith(Ref.make(0L)))(PiState(_, _))
      _ <- putStrLn("Welcome to ZIO Pi")
      compute <- computePi(piState).fork
      report <- reportStatus(piState).fork

      // we could also zip fibers and interrupt once zipped version.

      _ <- getStrLn.orDie *> compute.interrupt *> report.interrupt
    } yield 0

    // Multiple workers
    for {
      piState <- (Ref.make(0L).zipWith(Ref.make(0L)))(PiState(_, _))
      workers = List.fill(10)(computePi(piState))
      reporter = reportStatus(piState)
      fiber <- (ZIO.forkAll(workers).zipWith(reporter.fork))(_ zip _)
      _ <- getStrLn.orDie *> fiber.interrupt
    } yield ExitCode.success

  }
}

object Hangman extends App {

  import zio.console._
  import zio.random._
  import java.io.IOException

  /**
    * EXERCISE 13
    *
    * Implement an effect that gets a single, lower-case character from
    * the user.
    */
  lazy val getChoice: ZIO[Console, IOException, Char] =
    getStrLn.map(_.trim.toLowerCase.toList).flatMap {
      case char :: Nil => ZIO.succeed(char)
      case _ => putStrLn("Enter char!") *> getChoice
    }

  /**
    * EXERCISE 14
    *
    * Implement an effect that prompts the user for their name, and
    * returns it.
    */
  lazy val getName: ZIO[Console, IOException, String] = getStrLn

  /**
    * EXERCISE 15
    *
    * Implement an effect that chooses a random word from the dictionary.
    */
  lazy val chooseWord: ZIO[Random, Nothing, String] = ???

  /**
    * EXERCISE 17
    *
    * Implement the main game loop, which gets choices from the user until
    * the game is won or lost.
    */
  def gameLoop(ref: Ref[State]): ZIO[Console, IOException, Unit] = for {
    oldState <- ref.get
    char <- getChoice
    newState <- ref.updateAndGet(_.addChar(char))
    _ <- renderState(newState)
    loop <- guessResult(oldState, newState, char) match {
      case Incorrect => putStrLn("").as(true)
      case Lost => putStrLn("").as(false)
      case Won => putStrLn("").as(false)
      case Correct => putStrLn("").as(true)
      case Unchanged => putStrLn("").as(true)
    }
    _ <- if (loop) gameLoop(ref) else ZIO.unit
  } yield ()

  def renderState(state: State): ZIO[Console, Nothing, Unit] = {

    /**
      * f     n  c  t  o
      *  -  -  -  -  -  -  -
      *
      * Guesses: a, z, y, x
      */
    val word =
      state.word.toList
        .map(c => if (state.guesses.contains(c)) s" $c " else "   ")
        .mkString("")

    val line = List.fill(state.word.length)(" - ").mkString("")

    val guesses = " Guesses: " + state.guesses.mkString(", ")

    val text = word + "\n" + line + "\n\n" + guesses + "\n"

    putStrLn(text)
  }

  final case class State(name: String, guesses: Set[Char], word: String) {
    final def failures: Int = (guesses -- word.toSet).size

    final def playerLost: Boolean = failures > 10

    final def playerWon: Boolean = (word.toSet -- guesses).isEmpty

    final def addChar(char: Char): State = copy(guesses = guesses + char)
  }

  sealed trait GuessResult

  object GuessResult {

    case object Won extends GuessResult

    case object Lost extends GuessResult

    case object Correct extends GuessResult

    case object Incorrect extends GuessResult

    case object Unchanged extends GuessResult

  }

  def guessResult(oldState: State, newState: State, char: Char): GuessResult =
    if (oldState.guesses.contains(char)) GuessResult.Unchanged
    else if (newState.playerWon) GuessResult.Won
    else if (newState.playerLost) GuessResult.Lost
    else if (oldState.word.contains(char)) GuessResult.Correct
    else GuessResult.Incorrect

  /**
    * EXERCISE 18
    *
    * Implement hangman using `Dictionary.Dictionary` for the words,
    * and the above helper functions.
    */
  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    (for {
      word <- chooseWord
      name <- getName
      state = State(name, Set(), word)
      _ <- renderState(state)
      ref <- Ref.make(state)
      _ <- gameLoop(ref)
    } yield ExitCode.failure).orElse(ZIO.succeed(ExitCode.failure))
}

/**
  * GRADUATION PROJECT
  *
  * Implement a game of tic tac toe using ZIO, then develop unit tests to
  * demonstrate its correctness and testability.
  */
object TicTacToe extends App {

  import zio.console._

  sealed trait Mark {
    final def renderChar: Char = this match {
      case Mark.X => 'X'
      case Mark.O => 'O'
    }
    final def render: String = renderChar.toString
  }

  object Mark {

    case object X extends Mark

    case object O extends Mark

  }

  final case class Board private (value: Vector[Vector[Option[Mark]]]) {

    /**
      * Retrieves the mark at the specified row/col.
      */
    final def get(row: Int, col: Int): Option[Mark] =
      value.lift(row).flatMap(_.lift(col)).flatten

    /**
      * Places a mark on the board at the specified row/col.
      */
    final def place(row: Int, col: Int, mark: Mark): Option[Board] =
      if (row >= 0 && col >= 0 && row < 3 && col < 3)
        Some(
          copy(value = value.updated(row, value(row).updated(col, Some(mark))))
        )
      else None

    /**
      * Renders the board to a string.
      */
    def render: String =
      value
        .map(_.map(_.fold(" ")(_.render)).mkString(" ", " | ", " "))
        .mkString("\n---|---|---\n")

    /**
      * Returns which mark won the game, if any.
      */
    final def won: Option[Mark] =
      if (wonBy(Mark.X)) Some(Mark.X)
      else if (wonBy(Mark.O)) Some(Mark.O)
      else None

    private final def wonBy(mark: Mark): Boolean =
      wonBy(0, 0, 1, 1, mark) ||
        wonBy(0, 2, 1, -1, mark) ||
        wonBy(0, 0, 0, 1, mark) ||
        wonBy(1, 0, 0, 1, mark) ||
        wonBy(2, 0, 0, 1, mark) ||
        wonBy(0, 0, 1, 0, mark) ||
        wonBy(0, 1, 1, 0, mark) ||
        wonBy(0, 2, 1, 0, mark)

    private final def wonBy(row0: Int, col0: Int, rowInc: Int, colInc: Int, mark: Mark): Boolean =
      extractLine(row0, col0, rowInc, colInc).collect { case Some(v) => v }.toList == List
        .fill(3)(mark)

    private final def extractLine(row0: Int, col0: Int, rowInc: Int, colInc: Int): Iterable[Option[Mark]] =
      for {
        row <- (row0 to (row0 + rowInc * 2))
        col <- (col0 to (col0 + colInc * 2))
      } yield value(row)(col)
  }

  object Board {
    final val empty = new Board(Vector.fill(3)(Vector.fill(3)(None)))

    def fromChars(first: Iterable[Char], second: Iterable[Char], third: Iterable[Char]): Option[Board] =
      if (first.size != 3 || second.size != 3 || third.size != 3) None
      else {
        def toMark(char: Char): Option[Mark] =
          if (char.toLower == 'x') Some(Mark.X)
          else if (char.toLower == 'o') Some(Mark.O)
          else None

        Some(
          new Board(
            Vector(
              first.map(toMark).toVector,
              second.map(toMark).toVector,
              third.map(toMark).toVector
            )
          )
        )
      }
  }

  val TestBoard = Board
    .fromChars(List(' ', 'O', 'X'), List('O', 'X', 'O'), List('X', ' ', ' '))
    .get
    .render

  /**
    * The entry point to the game will be here.
    */
  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    putStrLn(TestBoard).as(ExitCode.success)
}
