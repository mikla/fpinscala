package performance.jmh

import java.util.{Collections, UUID}
import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Mode, OutputTimeUnit, Scope, State}
import collections.map._
import collections.search._
import cats.implicits._
import scala.collection.Searching._

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
class CollectionSearch {

  val NumberOfIds = 20000
  val legacyIds = (1 to NumberOfIds).toList
  val universalIds: Map[UUID, Int] = (1 to NumberOfIds).map(i => UUID.randomUUID() -> i).toMap

  @Benchmark
  def findOnListWithoutWithCatsEq: Map[UUID, Int] =
    universalIds.mapValues(lid => legacyIds.find(_ === lid)).flattenValues[Int]

  @Benchmark
  def findOnListWithoutCatsEq: Map[UUID, Int] =
    universalIds.mapValues(lid => legacyIds.find(_ == lid)).flattenValues[Int]

  @Benchmark
  def customBinarySearchOnSortedArray: Map[UUID, Int] = {
    val sortedArray = legacyIds.sorted.toArray
    universalIds.mapValues(lid => binarySearchFunctional(sortedArray, lid)).flattenValues[Int]
  }

  @Benchmark
  def findOnVector: Map[UUID, Int] = {
    val vector = legacyIds.toVector
    universalIds.mapValues(lid => vector.find(_ == lid)).flattenValues[Int]
  }

  @Benchmark
  def scalaBuiltInBinarySearchOnSortedArray: Map[UUID, Int] = {
    val array = legacyIds.toArray.sorted
    universalIds.mapValues(lid => array.binarySearch(lid)).flattenValues[Int]
  }

  @Benchmark
  def useMap: Map[UUID, Int] = {
    val m = legacyIds.map(e => (e, e)).toMap
    universalIds.mapValues { lid =>
      m.get(lid)
    }.flattenValues[Int]
  }

}
