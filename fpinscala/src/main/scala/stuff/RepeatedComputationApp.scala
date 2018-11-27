package stuff

import util.computation
import util.computation.repeated

object RepeatedComputationApp extends App {

  println(repeated(0, (x: Int) => x + 1, 5))

}
