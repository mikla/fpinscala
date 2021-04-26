package application

import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source

object TabsToCsvApp extends App {

  val file1 = "/Users/amiklushou/Documents/live-snapshots/live1/main/original/05c16e55-7cd2-4c2e-a0fa-43cbbc389fe3.txt"
  val tabs = Source.fromFile(file1).getLines.toList(3)

  val header = "persistence_id,sequence_nr,ser_id,ser_manifest,snapshot,snapshot_data,timestamp"
  val csv = tabs.split("\\|").map {
    case str =>
      val tstr = str.trim
      if (tstr == "null") ""
      else if (tstr == "8") "73"
      else tstr
  }.mkString(",")

  val file = new File(file1 + "out")
  val bw = new BufferedWriter(new FileWriter(file))

  bw.write(header)
  bw.write("\r")
  bw.write(csv)

  bw.close()

}
