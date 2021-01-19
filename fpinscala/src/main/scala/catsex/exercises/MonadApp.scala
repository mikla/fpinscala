package catsex.exercises

object MonadApp extends App {

  import cats._
  import cats.instances.list._
  import cats.instances.option._

  println {
    Monad[List].flatMap(List(1, 2, 3))(x => List(x, x))
  }

  println {
    Monad[Option].ifM(Some(true))(Option("true"), Option("false"))
  }

  println {
    Monad[List].ifM(List(true, false, true))(List("if true"), List("if false"))
  }

  case class Log[A](log: List[String], a: A)

  def toLogger[A, B](f: A => B)(message: String): A => Log[B] = a => Log(List(message), f(a))

  def execLoggers[A, B, C](a: A)(f1: A => Log[B])(f2: B => Log[C]): Log[C] = {
    val b = f1(a).a
    val log = f1(a).log
    val logc = f2(b)
    Log[C](log ::: logc.log, logc.a)
  }

  val add1Log: Int => Log[Int] = toLogger[Int, Int](_ + 1)("added one")
  println(add1Log(3))

  val multLog: Int => Log[Int] = toLogger[Int, Int](_ * 2)("mult by 2")
  println(multLog(3))

  println(execLoggers(3)(add1Log)(multLog))

  def bindLog[A, B](a: Log[A])(f: A => Log[B]): Log[B] = {
    val logb = f(a.a)
    Log(a.log ::: logb.log, logb.a)
  }

  println(bindLog(multLog(0))(add1Log))

  def execLoggersList[A](a: A)(l: List[A => Log[A]]): Log[A] = l.foldLeft(Log(List.empty, a)) {
    case (log, f) => bindLog(log)(f)
  }

  println(execLoggersList(3)(List(add1Log, multLog, x => Log(List("by 100"), x * 100))))

}
