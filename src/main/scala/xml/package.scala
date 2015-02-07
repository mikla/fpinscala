
import scala.xml.NodeSeq
import scalaz._

package object xml {


  implicit def xmlWithReadSupport(xml: NodeSeq): XmlReadsSupport =
    new XmlReadsSupport {
      override def readz[T](implicit r: Validation[NonEmptyList[String], T]): ValidationNel[String, T] = r
    }

  trait XmlReadsSupport {
    def readz[T](implicit r: Validation[NonEmptyList[String], T]): ValidationNel[String, T]
  }

}
