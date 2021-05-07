package circe_e

import io.circe._
import io.circe.generic.semiauto._
import spire.algebra.Order

object IntervalSeqDecodingApp extends App {

  case class IntervalSeq[T : Order](
    val belowAll: Boolean,
    private val values: Array[T],
    private val kinds: Array[Byte])

  implicit def intervalSeqEncoder[T : Encoder : Order]: Encoder[IntervalSeq[T]] = deriveEncoder[IntervalSeq[T]]

}
