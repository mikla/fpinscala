package xml

import java.util.UUID

import scala.xml._
import scala.xml.transform.{RewriteRule, RuleTransformer}

object XmlTransformers extends App {
  val xml = XML.load(this.getClass.getResource("/data1.xml"))

  val gozuid = UUID.randomUUID()
  val requid = UUID.randomUUID()
  val accid = UUID.randomUUID()

  def getGozuid(pref: String) = Attribute(pref, "GOZUID", gozuid.toString, scala.xml.Null)
  def getRequid(pref: String) = Attribute(pref, "ReqUID", gozuid.toString, scala.xml.Null)
  def getAccid(pref: String) = Attribute(pref, "ID", accid.toString, scala.xml.Null)

  val rule = new RewriteRule {
    override def transform(n: Node): Seq[Node] = n match {
      case e: Elem if e.label == "MetaInf" =>
        e.copy(attributes = e.attributes.append(getGozuid(e.prefix)).append(getRequid(e.prefix)))

      case e: Elem if e.label == "Accs" || e.label == "Acc" =>
        e.copy(attributes = e.attributes.append(getAccid(e.prefix)))
      case e => e
    }

  }

  println {
    new RuleTransformer(rule)(xml)
  }

}

