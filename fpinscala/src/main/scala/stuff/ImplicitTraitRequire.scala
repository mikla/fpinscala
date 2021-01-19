package stuff

trait Context {
  def timeZone: String
}

case class SchedulingContext(name: String, override val timeZone: String) extends Context
case class SelfServiceContext(name: String, order: Int, override val timeZone: String) extends Context

object ImplicitTraitRequire extends App {

  implicit val ctx = SchedulingContext("", "tz")

  def render(implicit C: Context) =
    println(C.timeZone)

  render

}
