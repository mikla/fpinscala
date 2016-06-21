import scalaz.Scalaz._
import scalaz._


object TravApp extends App {

  def mapOpt[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = {
    a.flatMap(aa => b map (bb => f(aa, bb)))
  }

  def traverse[A, B](list: List[A])(f: A => Option[B]): Option[List[B]] = {
    def loop(acc: Option[List[B]], rest: List[A]): Option[List[B]] = {
      if (rest.isEmpty) acc
      else loop(mapOpt(f(rest.head), acc)(_ :: _), rest.tail)

    }
    loop(Some(Nil): Option[List[B]], list) map (_.reverse)
  }

  def toOption(v: Int): Option[Int] = if (v == 0) None else Some(v)

  println {
    traverse(List(1, 2, 3))(toOption) // 1, 2, 3
  }

  println {
    traverse(List(1, 0, 3))(toOption) // None
  }

  // ==============
  def traverse2[A, B](list: List[A])(f: A => Option[B]): Option[List[B]] = {
    list.foldLeft((b: Option[List[B]]) => b)((g, a) => b => g(mapOpt(f(a), b)(_ :: _)))(Some(Nil: List[B]))
  }

  println {
    traverse2(List(1, 2, 3))(toOption) // 1, 2, 3
  }

  println {
    traverse2(List(1, 0, 3))(toOption) // None
  }

  // ================

  case class Bar(v: Int, opt: Option[String] = None)

  def toOptionBar(b: Bar): Option[Bar] = if (b.v == 0) None else Some(b)

  println {
    List(Bar(1), Bar(2), Bar(3)).map(toOptionBar).sequence
  }

  println {
    List(1, 0, 3).map(toOption).sequence
  }

  println {
    Applicative[Option].sequence(List(1, 2, 3).map(toOption))
  }

}
