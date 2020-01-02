package collections

object map {

  final implicit class MapUtilOps[K, V](val l: Map[K, V]) extends AnyVal {
    def flattenValues[T](implicit ev: V =:= Option[T]): Map[K, T] =
      l.collect { case (k, v) if ev(v).isDefined => k -> ev(v).get }
  }

}
