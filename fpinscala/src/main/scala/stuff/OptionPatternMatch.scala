package stuff

import stuff.OptionPatternMatch.Platform.{Amazon, Android}

object OptionPatternMatch extends App {

  trait Platform
  object Platform {
    case object iOS extends Platform
    case object Android extends Platform
    case object Amazon extends Platform
  }

  val platform: Option[Platform] = Some(Platform.Amazon)

  platform match {
    case Some(Android) => println("android")
    case Some(Platform.iOS) => println("ios")
    case Some(Amazon) => println("amazon")
  }

}
