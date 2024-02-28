package cats_effect

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.syntax.all._

import scala.concurrent.duration
import scala.concurrent.duration.FiniteDuration

object SeqTraverseApp extends App {

  List.empty[Int]
    .traverse(i => IO.delay(println(s"downloading $i")) >> IO.sleep(FiniteDuration(i, duration.SECONDS)) >> IO.pure(i))
    .flatTap(l => IO.delay(println(l)))
    .unsafeRunSync()


}
