package cats_effect

import java.time.temporal.Temporal

import cats.effect
import cats.effect.{IO, Resource}
import cats.effect.unsafe.implicits.global

import scala.concurrent.duration.DurationInt

object DelayedStartApp extends App {

  val res = for {
    _ <- Resource.eval(IO.delay(println("init")))
    _ <- effect.Temporal[IO].delayBy(IO.delay(println("Delayed by 10 sec")), 10.seconds).background
  } yield ()

  val res1 = for {
    _ <- Resource.eval(IO.delay(println("init")))
    _ <- (IO.sleep(30.seconds) >> IO.delay(println("Delayed by 30 sec"))).background
  } yield ()

  res1.allocated.unsafeRunSync()

  println("ok")

  while (true) {}

}
