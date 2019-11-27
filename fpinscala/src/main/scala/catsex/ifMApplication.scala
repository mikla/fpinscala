package catsex

import cats.implicits._

object ifMApplication extends App {

  case class Action1()
  case class Action2()

  println {
    List(
      Action1().some.whenA(1 == 2),
      Action2().some.whenA(1 == 1)
    )
  }

}
