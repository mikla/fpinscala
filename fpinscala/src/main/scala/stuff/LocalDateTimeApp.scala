package stuff

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LocalDateTimeApp extends App {

  val localDateTime = LocalDateTime.now()

  val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

  val formatted = formatter.format(localDateTime)

  val parsed = formatter.parse(formatted)

  println(parsed)

}
