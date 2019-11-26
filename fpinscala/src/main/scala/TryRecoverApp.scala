import catsex.free.Employee

import scala.util.{Failure, Try}

object TryRecoverApp extends App {

  Try(1.asInstanceOf[Employee]).recover {
    case e =>
      println("fucl")
  }

  readLine()

}
