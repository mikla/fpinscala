package stuff

object ProductIteratorApp extends App {

  case class Settings(setting1: Int, setting2: String)

  val x = Settings(1, "2")

  x.productIterator

}
