package fp_in_scala.io

/**
  * Given an impure function f: A => B, we can split it into two functions:
  * 1. A pure function A => D, where D is description of result f
  * 2. D => B which can be thought as an interpreter of these descriptions
  */
object IOApp extends App {

  def printlnLine(msg: String): IO =
    new IO {
      override def run: Unit = println(msg)
    }

}
