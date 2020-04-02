package stuff

import java.time.{LocalDateTime, ZoneId, ZoneOffset}

object AbsLimiter extends App {

//  2020-01-21T20:51

  println(LocalDateTime.of(2020, 1, 21, 20, 51, 0)
    .atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("Asia/Tbilisi")).toLocalDate)

}
