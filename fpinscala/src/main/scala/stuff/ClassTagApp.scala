package stuff

object ClassTagApp extends App {

  trait MB[T]

  trait Bound[T]
  type IntListBounded = Bound[MB[Int]]

  sealed abstract class Event[T: Bound]
  case class BoundedEvent[T: Bound](id: String) extends Event[T]
  case class BoundedEvent2[T: Bound](id: Int) extends Event[T]

  def decodeEvent(event: Event[IntListBounded]) = {
    event match {
      case e: BoundedEvent[IntListBounded] => println(e)
      case e: BoundedEvent2[IntListBounded] => println(e)
    }
  }


}
