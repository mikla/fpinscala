package application.cache

import cats.MonoidK
import cats.implicits._
import model.Employee

case class LocationsCacheSuites[+C[_] <: Cache[_]](map: Map[Long, CacheSuite[C]])

class CacheSuite[+C[_] <: Cache[_]](implicit M: MonoidK[Lambda[A => C[List[A]]]]) {
  val mutableEmployeesCache: C[List[Employee]] = M.empty[Employee]
}

object CacheSuite {

  implicit val mutableCacheMonoidK = new MonoidK[Lambda[A => MutableCache[List[A]]]] {
    override def empty[A] = MutableCache(List.empty)
    override def combineK[A](x: MutableCache[List[A]], y: MutableCache[List[A]]) =
      MutableCache(x.state.combineK(y.state))
  }

}
