package parallel

trait Par[A] {

}

object Par {

  /**
   * `unit` could begin evaluating its argument immediately ina separate (logical) thread,
   * or it could simply hold
   * onto its argument until get is called and begin evaluation then. But note that in this
   * example, if we want to obtain any degree of parallelism, we require that `unit` begin
   * evaluating its argument concurrently and return immediately*
   */
  def unit[A](a: A): Par[A] = ???

  def get[A](par: Par[A]): A = ???

  def map2[A, B, C](pa: Par[A], pb: Par[B])(f: (A, B) => C): Par[C] = ???

  /**
   * Means that the give Par[A] should run in separate logical thread.
   */
  def fork[A](a: => Par[A]): Par[A] = ???
}
