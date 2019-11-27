package application

object InjectHikariMetricsApp extends App {

  class Conf(url: String) {
    var metrics: String = _
    def setMetrics(m: String) = metrics = m
    def validate() = println(metrics)
  }

  class Connection(val hconf: Conf) {
    def debug = println(hconf.metrics)
  }


  val connection = new Connection(new Conf("localhost:5432"))

  val accessClass = classOf[Connection]

  val accessConfig = accessClass.getDeclaredField("hconf")
  accessConfig.setAccessible(true)

  val conf = accessConfig.get(connection).asInstanceOf[Conf]

  val accessMetrics = classOf[Conf].getDeclaredField("metrics")
  accessMetrics.setAccessible(true)
  accessMetrics.set(conf, "metrics")

  val accessSetMetrics = classOf[Conf].getDeclaredMethod("setMetrics", classOf[String])
  accessSetMetrics.setAccessible(true)
  accessSetMetrics.invoke(conf, "metrics2")

  println(conf.metrics)

  connection.debug

}
