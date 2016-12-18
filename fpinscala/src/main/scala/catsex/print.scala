package catsex

object print {

  implicit class PrintOps(value: Any) {
    def print() = println(value.toString)
    def print(msg: String) = println(s"$msg ${value.toString}")
  }

}
