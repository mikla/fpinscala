package performance.jmh

import java.util.concurrent.TimeUnit

import model.Employee
import org.openjdk.jmh.annotations._

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime, Mode.Throughput))
class MapValuesTraverseBenchmark {

  val employees: Map[String, List[Employee]] = (1 to 100000).map(id =>
    Employee(s"Name + ${id}", id, true)
  ).toList.groupBy(_.name)

  @Benchmark
  def mapValuesTest = mapValuesBench(employees)

  @Benchmark
  def mapValuesForceTest = mapValuesForceBench(employees)

  @Benchmark
  def mapTest = mapBench(employees)

  def mapValuesBench(employees: Map[String, List[Employee]]) =
    employees.mapValues(_.toString()).map(_._2 + "------")

  def mapValuesForceBench(employees: Map[String, List[Employee]]) =
    employees.mapValues(_.toString()).view.force.map(_._2 + "------")

  def mapBench(employees: Map[String, List[Employee]]) =
    employees.map {
      case (k, v) => k -> v.toString()
    }.map(_._2 + "------")

}
