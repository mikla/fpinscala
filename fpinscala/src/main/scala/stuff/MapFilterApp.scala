package stuff

object MapFilterApp extends App {

  case class UserId(id: Long)
  case class Assignment(userId: UserId, name: String)

  val assignments = List(Assignment(UserId(1), "a"), Assignment(UserId(1), "b"), Assignment(UserId(2), "c"))

  val map: Map[UserId, List[Assignment]] = Map(
    UserId(1) -> List(Assignment(UserId(1), "a"), Assignment(UserId(1), "b")),
    UserId(2) -> List(Assignment(UserId(1), "c"))
  )

  val x = map.map {
    case (k, v) if v.exists(_.name == "b") => k
  }

  def f(ids: UserId*) = ids.flatMap(id => map.getOrElse(id, Nil))

}
