package catsex.exercises

object MonadApp extends App {

  import cats._
  import cats.instances.list._
  import cats.instances.option._

  println {
    Monad[List].flatMap(List(1, 2, 3))(x ⇒ List(x, x))
  }

  println {
    Monad[Option].ifM(Some(true))(Option("true"), Option("false"))
  }

  println {
    Monad[List].ifM(List(true, false, true))(List("if true"), List("if false"))
  }

}
