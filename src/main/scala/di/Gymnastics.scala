package di

case class Id[A](a: A) {
  def map[B](f: A => B): Id[B] = Id(f(a))
  def flatMap[B](f: A => Id[B]): Id[B] = f(a)
}

/*case class Reader[A](rd: Context => A) {
  def map[B](f: A => B): Reader[B] = Reader(f compose rd)
  def flatMap[B](f: A => Reader[B]): Reader[B] = Reader(c => f(rd(c)) rd c)
}*/


// Hostname + port context
case class CReader[A](rd: (String, Int) => A) {
  def map[B](f: A => B): CReader[B] = ???
  def flatMap[B](f: A => CReader[B]): CReader[B] = ???
}

object CReader {
//  def point[A](a: => A): CReader[A] = CReader(_ => a)
}

case class State[Cx, A](run: Cx => (A, Cx)) {
  def map[B](f: A => B): State[Cx, B] =
    State(x => {
      val (a, t) = run(x)
      (f(a), t)
    })

  def flatMap[B](f: A => State[Cx, B]): State[Cx, B] =
    State(x => {
      val (a, t) = run(x)
      f(a) run t
    })

}

object Gymnastics extends App {

}
