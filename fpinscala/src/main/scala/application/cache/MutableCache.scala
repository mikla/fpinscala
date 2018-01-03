package application.cache

import cats.Monoid

/**
  * Mutable in-memory cache for some arbitrary state of type [[T]]
  *
  * @param initial initial state in the cache
  */
class MutableCache[T](initial: T) extends Cache[T] {

  private[this] var _state: T = initial

  /**
    * Update internal state with provided one.
    *
    * @return Previous state (before the update)
    */
  def update(newState: T): T = {
    val previousState = _state
    _state = newState // todo sync here
    previousState
  }

  def state: T = _state

  /* Reset state to the initial one */
  def reset(): Unit = _state = initial
}

object MutableCache {
  def apply[T](initial: T): MutableCache[T] = new MutableCache[T](initial)
  def empty[T](implicit M: Monoid[T]): MutableCache[T] = MutableCache[T](M.empty)
}
