package application

import application.cache.{Cache, MutableCache}
import model.Employee

object ReferenceByPointerApp extends App {

  case class Suites(map: Map[Long, Suite])

  class Suite {
    val mutableEmployees: MutableCache[List[Employee]] = MutableCache(List.empty)
  }

  val suitesCache = MutableCache[Suites](Suites(Map.empty))

  class SomeFacadeImpl(suites: Cache[Suites]) {

    // but we can still access mutable variable inside Suite cache.
    // which is baaad

    def show = println(suites.state.map.values.map(_.mutableEmployees.state))
  }

  val facade = new SomeFacadeImpl(suitesCache)

  facade.show

  val ns = suitesCache.state.copy(map = suitesCache.state.map + (1L -> new Suite()))

  suitesCache.update(ns)

  facade.show

  suitesCache.state.map.get(1).get.mutableEmployees.update(List(Employee("mi", 1, true)))

  facade.show

}
