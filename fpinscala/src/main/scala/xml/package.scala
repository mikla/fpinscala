
import scala.xml.NodeSeq
import scalaz._

package object xml {


  implicit def xmlWithReadSupport(xml: NodeSeq): XmlReadsSupport =
    new XmlReadsSupport {
      override def readz[T](implicit r: Readz[T]): ValidationNel[String, T] = r(xml)
    }


  trait XmlReadsSupport {
    def readz[T](implicit r: Readz[T]): ValidationNel[String, T]
  }


}
