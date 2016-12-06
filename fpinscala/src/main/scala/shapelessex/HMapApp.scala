package shapelessex

import shapeless.HMap

object HMapApp extends App {

  class BiMapIs[K, V]
  implicit val intToString = new BiMapIs[Int, String]
  implicit val stringToInt = new BiMapIs[String, Int]
  implicit val stringToBoolean = new BiMapIs[String, Boolean]

  val hm = HMap[BiMapIs](23 -> "foo", "foo" -> 23, "bar" -> true)

}
