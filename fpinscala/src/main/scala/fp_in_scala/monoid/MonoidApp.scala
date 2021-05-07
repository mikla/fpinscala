package fp_in_scala.monoid

import fp_in_scala.monoid.MonoidInstances._

object MonoidApp extends App with MonoidDefs {

  println(foldMap(List("a", "b", "c", "d"), stringMonoid)(identity))
  println(foldMapV(IndexedSeq("a", "b", "c", "d", "v", "z", "y"), stringMonoid)(identity))

}
