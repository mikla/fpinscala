package application.cache

import cats.implicits._
import CacheSuite._
import model.Employee

import scala.collection.concurrent.TrieMap
import scala.collection.mutable

object CacheApp extends App {

  val c: Cache[Int] = MutableCache[Int](0)

  val mutableSute: CacheSuite[MutableCache] = new CacheSuite[MutableCache]()
  mutableSute.mutableEmployeesCache.update(List(Employee("m", 1, true)))
  val suite: CacheSuite[Cache] = mutableSute
  println(suite.mutableEmployeesCache.state)

  val mapOfSuites: TrieMap[Long, CacheSuite[MutableCache]] = TrieMap()

  class SomeFacadeImpl(locationSuitesCache: TrieMap[Long, _ <: CacheSuite[Cache]]) {
    def show = println(locationSuitesCache.values.map(_.mutableEmployeesCache.state.toString()))
  }

  val facade = new SomeFacadeImpl(mapOfSuites)

  facade.show

  // let's go deeper :)

  val suites: MutableCache[LocationsCacheSuites[MutableCache]] =
    MutableCache[LocationsCacheSuites[MutableCache]](LocationsCacheSuites(Map.empty))

  class SomeFacade2(locationsCacheSuites: Cache[LocationsCacheSuites[Cache]]) {
    def show = println(locationsCacheSuites.state.map.values.map(_.mutableEmployeesCache.state.toString()))
  }

  val facade2 = new SomeFacade2(suites)

  facade2.show

  suites.update(suites.state.copy(map = suites.state.map + (1L -> new CacheSuite[MutableCache]())))

  facade2.show

  suites.state.map(1L).mutableEmployeesCache.update(List(Employee("1", 1, true)))

  facade2.show

}
