package typeclasses.dialectic

// example from the Aaron Levin https://www.youtube.com/watch?v=0wxGrf8toWk&feature=youtu.be
object events {

  case class Click(user: String, page: String)
  case class Play(user: String, trackId: Long)
  case class Pause(user: String, trackId: Long, ts: Long)

  type EndOfList = Unit

  type Events = (Click, (Play, (Pause, EndOfList)))

  trait Named[E] {
    val name: String
  }

  implicit val namedClick = new Named[Click] {
    override val name: String = "click"
  }

  implicit val namedPlay = new Named[Play] {
    override val name: String = "play"
  }

  implicit val namedPause = new Named[Pause] {
    override val name: String = "pause"
  }

  implicit val baseCaseName = new Named[EndOfList] {
    override val name: String = ""
  }

  implicit def inductionCaseNamed[E, Tail](
    implicit
    n: Named[E],
    t: Named[Tail]
  ) = new Named[(E, Tail)] {
    override val name: String = s"${n.name}, ${t.name}"
  }

  def getNamed[E](implicit N: Named[E]): String = N.name

}
