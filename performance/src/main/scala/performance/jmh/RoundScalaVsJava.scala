package performance.jmh

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Mode, OutputTimeUnit, Scope, State}

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime, Mode.SampleTime, Mode.Throughput))
class RoundScalaVsJava {

  val doubles = (1 to 10000000).map(_ => Math.random() * 1000).toArray

  def roundDoubleScala(value: Double, digits: Int): Double =
    (math.rint(value * math.pow(10d, digits))) / math.pow(10d, digits)

  def roundDoubleJava(value: Double, digits: Int): Double =
    (Math.rint(value * Math.pow(10d, digits))) / Math.pow(10d, digits)

  @Benchmark
  def checkScala =
    doubles.map(roundDoubleScala(_, 2))

  @Benchmark
  def checkJava =
    doubles.map(roundDoubleJava(_, 2))

}
