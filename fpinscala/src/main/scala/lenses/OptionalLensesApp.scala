package lenses

import monocle.Lens
import monocle.macros.GenLens
import monocle.function.Cons.headOption
import monocle.std.option._

object OptionalLensesApp extends App {

  val employee1 = Employee1("ml", 1, false, Some(Wrapper(Algo(1, 1))))
  val employee2 = Employee1("ml", 1, false, None)

  val modified1 = Employee1.solid.set(2)(employee1)
  val modified2 = Employee1.solid.set(2)(employee2)

  println(modified1)
  println(modified2)

}


case class Employee1(name: String, number: Int, manager: Boolean, hoursAlgo: Option[Wrapper] = None)
object Employee1 {
  val hoursAlgo: Lens[Employee1, Option[Wrapper]] = GenLens[Employee1](_.hoursAlgo)
  val solid = hoursAlgo composePrism some composeLens Wrapper.algo composeLens Algo.fluidLens
}

case class Wrapper(value: Algo)
object Wrapper {
  val algo: Lens[Wrapper, Algo] = GenLens[Wrapper](_.value)
}

case class Algo(solid: Long, fluid: Long)
object Algo {
  val solidLens: Lens[Algo, Long] = GenLens[Algo](_.solid)
  val fluidLens: Lens[Algo, Long] = GenLens[Algo](_.fluid)

}
