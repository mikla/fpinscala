package performance.jmh.collections

import scala.util.Random
import cats.syntax.all._

object col {

  final implicit class IterableCollectionUtilOps[A, C[X] <: Iterable[X]](val xs: C[A]) extends AnyVal {
    def toIdMap[Id](extractor: A => Id): Map[Id, A] =
      xs.view.map(e => extractor(e) -> e).toMap

    def toIdMap2[Id](extractor: A => Id): Map[Id, A] = {
      val builder = Map.newBuilder[Id, A]
      for (e <- xs)
        builder += extractor(e) -> e
      builder.result()
    }

    def toIdSet[Id](extractor: A => Id): Set[Id] =
      xs.view.map(extractor).toSet

    def randomElement: Option[A] =
      if (xs.isEmpty) None
      else Some(xs.toIndexedSeq(Random.nextInt(xs.size)))

    def toNonEmpty: Option[C[A]] = xs.headOption.as(xs)
  }

}
