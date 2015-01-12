package parallel

trait Par[A] {

}

object Par {
  def unit[A](a: => A): Par[A] = ???
  def get[A](par: Par[A]): A = ???
}
