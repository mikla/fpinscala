package performance.jmh

import java.util.concurrent.TimeUnit

import model.Employee
import org.openjdk.jmh.annotations._

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime, Mode.SampleTime, Mode.Throughput))
class MapValuesBenchmark2 {

  import MapValuesBenchmark2._

  val employees: Map[Int, Int] = (1 to 100000).map(id => id -> id).toMap

  @Benchmark
  def mapValuesTest = mapValuesBench(employees)

  @Benchmark
  def mapValuesForceTest = mapValuesForceBench(employees)

  @Benchmark
  def mapTest = mapBench(employees)

  def mapValuesBench(employees: Input) =
    employees.mapValues(_.toString()).values.size

  def mapValuesForceBench(employees: Input) =
    employees.mapValues(_.toString()).view.force.values.size

  def mapBench(employees: Input) =
    employees.map {
      case (k, v) => k -> v.toString()
    }.values.size

}

object MapValuesBenchmark2 {
  type Input = Map[Int, Int]
}
