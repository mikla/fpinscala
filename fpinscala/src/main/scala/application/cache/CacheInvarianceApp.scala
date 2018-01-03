package application.cache

object CacheInvarianceApp extends App {

  trait Cache[T] {
    def state: T
  }

  class MutableCache[T](initial: T) extends Cache[T] {
    private[this] var _state: T = initial
    def state: T = _state
  }

}
