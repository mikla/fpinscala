package performance.jmh

import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Mode, OutputTimeUnit, Scope, State}
import performance.jmh.col._

import java.time.Instant
import java.util.concurrent.TimeUnit

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime, Mode.SampleTime, Mode.Throughput))
class ToIdMap {

  case class Smth(id: Long, name: String, time: Instant)

  val employeesSmth = (1 to 1000)
    .flatMap(id =>
      (1 to 1000)
        .map(i => Smth(id, s"$id + ${i.toString}", Instant.now())))

  @Benchmark
  def toIdMapDefaultTest =
    employeesSmth.toIdMap(_.id)

  @Benchmark
  def toIdMap2Test =
    employeesSmth.toIdMap2(_.id)

}
