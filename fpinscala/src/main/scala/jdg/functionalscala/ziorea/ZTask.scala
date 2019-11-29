package jdg.functionalscala.ziorea

// This shit eventually hit stackoverflow
final case class ZTask[+A](code: () => A) { self =>
  def map[B](f: A => B): ZTask[B] = ZTask(() => f(code()))
  def flatMap[B](f: A => ZTask[B]): ZTask[B] = ZTask(() => f(code()).code())
  def zip[B](that: ZTask[B]): ZTask[(A, B)] =
    self.flatMap(a => that.map(b => a -> b))
  def *>[B](that: ZTask[B]): ZTask[B] = (self zip that) map (_._2)
  def <*[B](that: ZTask[B]): ZTask[A] = (self zip that) map (_._1)
  def either: ZTask[Either[Throwable, A]] =
    ZTask { () =>
      try Right(code())
      catch {
        case t: Throwable => Left(t)
      }
    }
}

object ZTask {

  def succeed[A](a: A): ZTask[A] = ZTask(() => a)
  def fail = ???

}
