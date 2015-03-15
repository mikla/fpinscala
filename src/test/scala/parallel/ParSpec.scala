package parallel

import java.util.concurrent.Executors

import org.scalatest.{Matchers, FlatSpec}

class ParSpec extends FlatSpec with Matchers {

  val PoolSize = 4
  val pool = Executors.newFixedThreadPool(PoolSize)

  "Parallel.sum" should "sum of Ints" in {
    val seqSum = Parallel.sum3(IndexedSeq(1, 2, 3, 4))
    assert(seqSum(pool).get() equals 10)
  }

}
