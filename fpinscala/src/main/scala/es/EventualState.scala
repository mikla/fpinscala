package es

sealed trait EventualState[+S] {

  def isInitialized: Boolean = this match {
    case NotInitialized => false
    case Initialized(_) => true
  }

  def toOption: Option[S] = this match {
    case NotInitialized => None
    case Initialized(state) => Some(state)
  }

  def get: S = toOption.get
}
case object NotInitialized extends EventualState[Nothing]
case class Initialized[+S](state: S) extends EventualState[S]
