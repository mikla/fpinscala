package stuff

import shapeless.Generic

abstract class Root(isPattern: Boolean)

case class Chile(id: String) extends Root(true)

object GenericInheritedClasses extends App {

  println(Generic[Chile].to(Chile("1")))


}
