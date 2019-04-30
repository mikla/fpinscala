package monix.observable

import monix.execution.Scheduler.Implicits.global
import scala.concurrent.duration._

object ObservableApp extends App {

  import monix.reactive._

  // Nothing happens here, as observable is lazily
  // evaluated only when the subscription happens!
  val tick = {
    Observable.interval(1.second)
      // common filtering and mapping
      .filter(_ % 2 == 0)
      .map(_ * 2)
      // any respectable Scala type has flatMap, w00t!
      .flatMap(x => Observable.fromIterable(Seq(x,x)))
      // only take the first 5 elements, then stop
      .take(5)
      // to print the generated events to console
      .dump("Out")
  }

}
