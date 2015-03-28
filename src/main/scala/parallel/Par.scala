package parallel

import java.util.concurrent.{Callable, TimeUnit, Future, ExecutorService}

object Par {

  /**
   * `unit` could begin evaluating its argument immediately ina separate (logical) thread,
   * or it could simply hold
   * onto its argument until get is called and begin evaluation then. But note that in this
   * example, if we want to obtain any degree of parallelism, we require that `unit` begin
   * evaluating its argument concurrently and return immediately*
   */
  def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  def run[A](s: ExecutorService)(par: Par[A]): Future[A] = par(s)

  def map2[A, B, C](pa: Par[A], pb: Par[B])(f: (A, B) => C): Par[C] = (es: ExecutorService) => {
    val af = pa(es)
    val bf = pb(es)
    UnitFuture(f(af.get, bf.get))
  }

  /**
   * Means that the give Par[A] should run in separate logical thread.
   */
  def fork[A](a: => Par[A]): Par[A] = (es: ExecutorService) => {
    es.submit(new Callable[A] {
      override def call(): A = a(es).get
    })
  }

  def asyncF[A, B](f: A => B): A => Par[B] = a => lazyUnit(f(a))

  def sortPar(parList: Par[List[Int]]): Par[List[Int]] = map2(parList, unit(()))((a, _) => a.sorted)

  def map[A, B](pa: Par[A])(f: A => B): Par[B] = map2(pa, unit(()))((a, _) => f(a))

  def sortParViaMap(parList: Par[List[Int]]) = map(parList)(_.sorted)

  def sequence[A](ps: List[Par[A]]): Par[List[A]] = ps match {
    case Nil => unit(Nil)
    case x :: xs => map2(x, fork(sequence(xs)))(_ :: _)
  }

  def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] = {
    val fbs: List[Par[B]] = ps.map(asyncF(f))
    sequence(fbs)
  }

  def parFilter[A](ps: List[A])(predicate: A => Boolean): Par[List[A]] = {
    val l = ps map asyncF(a => if (predicate(a)) List(a) else List())
    map(sequence(l))(_.flatten)
  }

  def equal[A](e: ExecutorService)(p: Par[A], p2: Par[A]): Boolean = p(e).get == p2(e).get

  /**
   * UnitFuture doesn't perform any computation, It's already done.
   */
  private case class UnitFuture[A](get: A) extends Future[A] {
    override def cancel(mayInterruptIfRunning: Boolean): Boolean = false
    override def isCancelled: Boolean = false
    override def get(timeout: Long, unit: TimeUnit): A = get
    override def isDone: Boolean = true
  }
}
