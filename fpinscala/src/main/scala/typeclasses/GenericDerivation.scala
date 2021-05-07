package typeclasses

trait Parser[A] {
  def apply(s: String): Option[A]
}

trait Monad[M[_]] {
  def unit[A](x: A): M[A]
  def flatMap[A, B](m: M[A])(f: A => M[B]): M[B]
}

object GenericDerivation extends App {

  implicit val listMonad: Monad[List] = new Monad[List] {
    override def unit[A](x: A): List[A] = ???
    override def flatMap[A, B](m: List[A])(f: (A) => List[B]): List[B] = m.flatMap(f)
  }

  implicit class ClOps[A](l: List[A])(implicit val m: Monad[List]) {
    def fl[B](f: A => List[B]): List[B] = m.flatMap(l)(f)
  }

  println(List(1, 2).fl(_ => List(1)))

  implicit def hconsParser[H : Parser]: Parser[H] = ???

}
