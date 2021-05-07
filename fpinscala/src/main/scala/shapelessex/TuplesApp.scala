package shapelessex

import shapeless.PolyDefns.~>
import shapeless._
import shapeless.syntax.std.tuple._

object TuplesApp extends App {

  val t = (23, "foo", true)

  t.head // 23

  println(t.drop(1))

  println(t.split(1))

  // type Id[+T] = T
  object option extends (Id ~> Option) {
    override def apply[T](f: Id[T]): Option[T] = Option(f)
  }

  println((23, "foo", true).toList)

  // zipper
  import syntax.zipper._
  val l = (23, ("foo", true), 2.0).toZipper.right.down.put("bar").root.reify
  println(l)

}
