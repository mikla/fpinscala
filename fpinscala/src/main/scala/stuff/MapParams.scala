package stuff

object MapParams extends App {

  val params: Map[String, String] = Map("userId" -> "uuid", "start" -> "10.09.2016", "end" -> "10.09.2017")

  val str = params.map {
    case (k, v) => s"$k=$v"
  }.mkString("&")



}
