package xml


import scala.xml.NodeSeq
import scalaz.ValidationNel

trait Readz[T] { self =>
  def apply(xml: NodeSeq): ValidationNel[String, T]
}

object Readz {
  def at[T](path: XmlPath)(implicit r: Readz[T]): Readz[T] = Readz(node => r(path(node)))

  def apply[T](parseFn: NodeSeq => ValidationNel[String, T]): Readz[T] = new Readz[T] {
    override def apply(xml: NodeSeq): ValidationNel[String, T] = parseFn(xml)
  }
}
