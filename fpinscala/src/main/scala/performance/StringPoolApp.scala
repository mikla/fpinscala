package performance

import java.io.File

import scala.io.{Source, StdIn}

object StringPoolApp extends App {

  case class Row(note: String)

  val rows = (1 to 1000).map(_ => Row("default-note"))

  val rowsFromFile = Source
    .fromFile(new File("/Users/amiklushou/projects/fpinscala/fpinscala/src/main/scala/performance/rows"))
    .getLines.toList.map { str =>
    Row(str.intern())
  }

  StdIn.readLine()

  println(rows)
  println(rowsFromFile)
}
