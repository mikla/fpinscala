package cats

object ApplicativeApp extends App {

  import cats._
  import cats.std.list._
  import cats.std.option._

  // just pure

  (Applicative[List] compose Applicative[Option]).pure(1) // List(Some(1))

}
