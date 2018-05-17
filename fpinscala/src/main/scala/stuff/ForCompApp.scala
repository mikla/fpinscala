package stuff

object ForCompApp extends App {

  val x: Option[Int] = None
  val y: Option[Int] = Some(2)

  println(for {
    xx <- x
    t = xx + 4
    yy <- y
  } yield xx + yy)

}
