package fp_in_scala.parallel

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.util.concurrent.Executors

class ParSpec extends AnyFlatSpec with Matchers {

  val PoolSize = 4
  val pool = Executors.newFixedThreadPool(PoolSize)

  "Parallel.sum" should "sum of Ints" ignore {
    val seqSum = Parallel.sum3(IndexedSeq(1, 2, 3, 4))
    //    assert(seqSum(pool).get() equals 10)
  }

  "sequence" should "return Par[List[A]]" ignore {

  }

  "parFilter" should "filter List in fp_in_scala.parallel" ignore {
    val res = Par.parFilter(List(1, 2, 3, 4))(_ > 2)
    //    res(pool).get() should equal (List(3, 4))
  }

}
