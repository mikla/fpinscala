package xml

import scala.xml.{Node, NodeSeq}
import scalaz.ValidationNel
import scalaz._
import Scalaz._

sealed trait XmlPath {
  def apply(xml: NodeSeq): NodeSeq

  // TODO
  def readz[T](implicit r: ValidationNel[String, T]): ValidationNel[String, T] = {

    val xmlPath = this.toString
//    r.apply(this())
    "ok".failureNel

//     Readz.at[T](this)(r)
     ???
//    Readz.at[T](this)(r)
  }// Readz.at[T](this)(r)

  def \ (segment: String) = XmlPathSegment(segment, this)
  def filter (predicate: Node => Boolean) = XmlPathFilter(predicate, this)
}

object RootPath extends XmlPath {
  def apply(xml: NodeSeq) = xml
}

case class XmlPathSegment(path: String, prev: XmlPath) extends XmlPath {
  def apply(xml: NodeSeq) = prev(xml) \ path
}

case class XmlPathFilter(predicate: Node => Boolean, prev: XmlPath) extends XmlPath {
  def apply(xml: NodeSeq) = prev(xml).filter(predicate)
}
