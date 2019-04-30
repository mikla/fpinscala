package stuff

import java.time.{LocalDateTime, ZoneId, ZonedDateTime}

object DateTimeApp extends App {

  val zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())

  println(zonedDateTime)
  println(zonedDateTime.toInstant)

}
