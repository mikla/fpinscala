package jdg.functionalscala

import java.sql.ResultSet

import zio._
import zio.stm._
import zio.stream.{Sink, ZStream}

object Day3 extends App {

  import zio.console._
  import zio.stm._

  class Lock private(tref: TRef[Boolean]) {
    def acquire: UIO[Unit] = (for {
      v <- tref.get
      _ <- if (v) STM.retry else tref.set(true)
    } yield ()).commit

    def release: UIO[Unit] = (for {
      v <- tref.get
      _ <- if (v) tref.set(false) else STM.dieMessage("Unowned lock")
    } yield ()).commit
  }

  object Lock {
    def make: UIO[Lock] = TRef.make(false).map(new Lock(_)).commit
  }

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = ???

  def exampleRef = {
    def swap[A](ref1: Ref[A], ref2: Ref[A]): UIO[Unit] =
      for {
        v1 <- ref1.get
        v2 <- ref1.get
        _ <- ref1.set(v2)
        _ <- ref2.set(v1)
      } yield ()

    for {
      ref1 <- Ref.make(100)
      ref2 <- Ref.make(0)
      fiber1 <- swap(ref1, ref2).repeat(Schedule.recurs(100)).fork
      fiber2 <- swap(ref2, ref1).repeat(Schedule.recurs(100)).fork
      _ <- (fiber1 zip fiber2).join
      value <- ref1.get.zipWith(ref2.get)(_ + _)
    } yield value
  }

  def exampleSTM = {

    //
    def swap[A](ref1: TRef[A], ref2: TRef[A]): UIO[Unit] =
      (for {
        v1 <- ref1.get
        v2 <- ref1.get
        _ <- ref1.set(v2)
        _ <- ref2.set(v1)
      } yield ()).commit

    for {
      ref1 <- TRef.make(100).commit
      ref2 <- TRef.make(0).commit
      fiber1 <- swap(ref1, ref2).repeat(Schedule.recurs(100)).fork
      fiber2 <- swap(ref2, ref1).repeat(Schedule.recurs(100)).fork
      _ <- (fiber1 zip fiber2).join
      value <- ref1.get.zipWith(ref2.get)(_ + _).commit
    } yield value
  }

  // Implement TicTacToe

  object sharding extends App {
    /**
      * Create N workers reading from a Queue, if one of them fails,
      * then wait for the other ones to process the current item, but
      * terminate all the workers.
      */
    def shard[R, E, A](queue: Queue[A], n: Int, worker: A => ZIO[R, E, Unit]): ZIO[R, E, Nothing] = {
      if (n <= 0) ZIO.dieMessage(s"Expect n > 0")
      else {
        val queueWorkers = ZIO.uninterruptible(ZIO.interruptible(queue.take) flatMap worker).forever
        val workers = ZIO.forkAll(List.fill(n)(queueWorkers))
        for {
          fiber <- workers
          _ <- fiber.join
        } yield 0
      }
      ???
    }

    def run(args: List[String]) = ???
  }

  object zio_stream extends App {

    import zio.stream.Stream

    /**
      * EXERCISE 1
      *
      * Create a stream containing 1, 2, and 3 using `Stream.apply`
      */
    val streamStr: Stream[Nothing, Int] = Stream(1, 2, 3)

    /**
      * EXERCISE 2
      *
      * Create a stream using `Stream.fromIterable`
      */
    val stream1: Stream[Nothing, Int] = Stream.fromIterable(List(1, 2, 3))

    /**
      * EXERCISE 3
      *
      * Create a stream using `Stream.fromChunk`.
      */
    val chunk: Chunk[Int] = Chunk(43 to 100: _*)
    val stream2: Stream[Nothing, Int] = Stream.fromChunk(chunk)

    /**
      * EXERCISE 4
      *
      * Make a queue and use it to create a stream using `Stream.fromQueue`.
      */
    def stream3[A](q: Queue[A]): Stream[Nothing, A] = Stream.fromQueue(q)

    /**
      * EXERCISE 5
      *
      * Create a singleton stream from an effect producing a String using
      * `Stream.fromEffect`.
      */
    val stream4: ZStream[Console, Exception, String] = ZStream.fromEffect(console.getStrLn).forever

    /**
      * EXERCISE 6
      *
      * Create a stream of ints that starts from 0 until 42, using `Stream#unfold`.
      */
    val stream5: Stream[Nothing, String] = Stream.unfold(0)(i => Some(s"Index: $i", i + 1))

    /**
      * EXERCISE 7
      *
      * Using `Stream.unfoldM`, create a stream of lines of input from the user,
      * terminating when the user enters the command "exit" or "quit".
      */

    import java.io.IOException
    import zio.console.getStrLn

    val stream6: ZStream[Console, IOException, String] = ZStream.unfoldM(()) { _ =>
      getStrLn.map {
        case "quit" => None
        case x => Some((x, ()))
      }
    }

    /**
      * EXERCISE 8
      *
      * Using `Stream#withEffect`, log every element of `stream1` to the console.
      */
    val loggedInts: ZStream[Console, Nothing, Int] = ??? // ZStream.fromEffect(getStrLn >>= putStrLn)

    /**
      * EXERCISE 9
      *
      * Using `Stream#filter`, filter for just the even numbers in `stream1`.
      */
    val evenNumbers: Stream[Nothing, Int] = ???

    /**
      * EXERCISE 10
      *
      * Using `Stream#takeWhile`, take the numbers that are less than 10 from
      * `stream1`.
      */
    val lessThan10: Stream[Nothing, Int] = ???

    /**
      * EXERCISE 11
      *
      * Using `Stream#foreach`, Print out each value in the stream.
      */
    def printAll[A](s: Stream[Nothing, A]): ZIO[Console, Nothing, Unit] = ???

    /**
      * EXERCISE 12
      *
      * Using `Stream#map`, convert every `Int` into a `String`.
      */
    val toStr: Stream[Nothing, String] = ???

    /**
      * EXERCISE 13
      *
      * Using `Stream#merge`, merge two streams together.
      */
    val mergeBoth: Stream[Nothing, Int] = ???

    /**
      * EXERCISE 14
      *
      * Using `Sink#readWhile`, create a `Sink` that takes an input of type String
      * and check if it's non-empty
      */
    val sink: Sink[Nothing, String, String, String] = ???

    /**
      * EXERCISE 15
      *
      * Using `Stream#run`, run the stream with `sink` get a list of the first
      * non-empty strings.
      */
    val stream: ZIO[Any, Nothing, Unit] = Stream("Forest").forever.runDrain

    val firstNonEmpty: ZIO[Any, Nothing, List[String]] = ???

    // Parsing Json

    /*
    type Json
    type DomainObject
    val jsonStream: Stream[Nothing, Json] = ???
    def parseToDomain(json: Json): Either[Exception, DomainObject] = ???
    def processDomain(domain: DomainObject): UIO[Unit] = ???
    def logError(error: Exception): UIO[Unit] = ???
    jsonStream.map(parseToDomain).flatMapPar(100) { // do it in parallel
      case Left(error) => Stream.fromEffect(logError(error) *> ZIO.succeed(error))
      case Right(value) => Stream.fromEffect(processDomain(value)).drain
    }.runDrain

     */

    override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
      stream map (_ => 0)
  }


  object dependencies {

    /**
      * EXERCISE 1
      *
      * Make the `LiveUserStore` depend on a `Database` by having the
      * `LiveUserStore` trait extend `Database`.
      */
    trait LiveUserStore extends UserStore {
      val userStore: UserStore.Service = new UserStore.Service {
        def getUserById(id: Long): Task[UserProfile] = ???
      }
    }

    // ZIO[R, E, A]
    // Introduce the env. effect: ZIO.accessM (ZIO.environment)
    // Eliminate the enveromental effect: ZIO#provide

    type Result
    trait Query {
      def q(sql: String): Task[Result]
    }

    def query(sql: String): ZIO[Query, Throwable, Result] =
      ZIO.accessM[Query](env => env.q(sql))

    query("SQL ").provide((sql: String) => ???)
    query("SQL ").provideSome((sql: String) => ???) // you can provide part of environment

    /**
      * EXERCISE 2
      *
      * Effectfully create a `Database` module inside `Task`. In a real
      * implementation, this method might actually perform the database
      * connection.
      */
    def connect(connectionUrl: String): Task[Database] =
      Task.effect {
        val dbConn = ???

        ???
      }

    /**
      * EXERCISE 3
      *
      * Define a `UserStore` in terms of a `Database`.
      */
    lazy val userService: ZIO[Database, Nothing, UserStore] =
      ZIO.accessM[Database](
        env =>
          UIO {
            ???
          }
      )

    /**
      * EXERCISE 4
      *
      * Using `provideSomeM`, eliminate the `UserStore` dependency.
      */
    lazy val myProgram: ZIO[UserStore, Throwable, Unit] = ??? // Assume implemented
    lazy val eliminated: ZIO[Database, Throwable, Unit] = ???

    trait Database {
      val database: Database.Service
    }
    object Database {
      trait Service {
        def query(query: String): Task[ResultSet]
      }
    }

    trait UserStore {
      val userStore: UserStore.Service
    }
    object UserStore {
      trait Service {
        def getUserById(id: Long): Task[UserProfile]
      }
    }
    case class UserProfile(name: String, age: Int, address: String)
  }


}
