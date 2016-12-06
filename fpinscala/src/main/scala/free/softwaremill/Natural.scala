package free.softwaremill

/**
  * Natural transformation
  */
trait Natural[F[_], G[_]] {
  def apply[A](f: F[A]): G[A]
}