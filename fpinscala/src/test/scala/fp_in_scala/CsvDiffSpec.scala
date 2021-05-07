package fp_in_scala

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

import scala.io.Source

class CsvDiffSpec extends AnyFlatSpec with Matchers {

  "two sorted csv files" should "match each other" in {

    val file1 = "/Users/amiklushou/Documents/live-snapshots/live1/main/05c16e55-7cd2-4c2e-a0fa-43cbbc389fe3.csv"
    val file2 =
      "/Users/amiklushou/Documents/live-snapshots/live1/main/original/05c16e55-7cd2-4c2e-a0fa-43cbbc389fe3.csv"

//    val serIdIndex = 5
    val serIdIndex = 2

    val file1Lines = Source.fromFile(file1).getLines
    val file2Lines = Source.fromFile(file2).getLines

    file1Lines.zip(file2Lines).drop(1).foreach { case (f1line, f2line) =>
      val f1cols = f1line.split(",").toList.zipWithIndex
      val f2cols = f2line.split(",").toList.zipWithIndex

      f1cols.zip(f2cols).foreach { case ((l, li), (r, ri)) =>
        if (li != serIdIndex)
          l mustBe r
        else {
          l mustBe "73"
          r mustBe "8"
        }
      }
    }

  }

  "looking for missing employees" should "" in {
    val file1 = "/Users/amiklushou/Documents/live-snapshots/live1/main/output-000001.csv"
    val file2 = "/Users/amiklushou/Documents/live-snapshots/live1/main/output-000002.csv"

    val employeesFile1 = "/Users/amiklushou/Downloads/passivated-employees-30"
    val employeesFile2 = "/Users/amiklushou/Downloads/passivated-employees-29"

    val file1Lines = Source.fromFile(file1).getLines.toList
    val file2Lines = Source.fromFile(file2).getLines.toList

    val employees1 = Source.fromFile(employeesFile1).getLines.toList.map(_.trim)
    val employees2 = Source.fromFile(employeesFile2).getLines.toList.map(_.trim)

    val allEmployees = (employees1 ++ employees2).distinct

    val report = allEmployees .map { persistenceId =>
      val containsIn1 = file1Lines.exists(_.contains(persistenceId))
      val containsIn2 = file2Lines.exists(_.contains(persistenceId))
      (persistenceId, containsIn1 || containsIn2)
    }.sortBy(_._2)

    println("Total: " + report.size)
    println("Found: " + report.count(_._2))
    println("Not Found: " + (report.size - report.count(_._2)))

    println(report.mkString("\n"))
  }

}
