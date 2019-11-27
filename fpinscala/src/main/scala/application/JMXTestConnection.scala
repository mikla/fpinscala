package application

import javax.management.remote.{JMXConnectorFactory, JMXServiceURL}
import scala.collection.JavaConverters._
import javax.management.{MBeanAttributeInfo, ObjectName}

import scala.util.Try

object JMXTestConnection extends App {
  // we listen to our own JMX agent!
  val url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9010/jmxrmi")
  val connector = JMXConnectorFactory.connect(url)
  val server = connector.getMBeanServerConnection()
  val all = server.queryMBeans(null, null).asScala

  println(all.map(_.getObjectName)
    .map(name => s"$name\n" + attributes(name)))

//   we can also call the JMX methods: "gc", "change" (custom MBean):
//  server.invoke(ObjectName.getInstance("java.lang:type=Memory"), "gc", null, null)
//  server.invoke(ObjectName.getInstance("com.sderosiaux:type=Metric"), "change", Array(new Integer(18)), null)
//

  // helpers
  private def attributes(name: ObjectName) = {
    server.getMBeanInfo(name).getAttributes.toList.map(attribute(name, _)).mkString("\n")
  }
  private def attribute(name: ObjectName, attr: MBeanAttributeInfo) = {
    s"- ${attr.getName} - ${attr.getDescriptor} - (${attr.getType}) = ${attributeValue(name, attr)}"
  }
  private def attributeValue(name: ObjectName, attr: MBeanAttributeInfo) = {
    // it's possible getAttribute throws an exception, see the output below
    Try(server.getAttribute(ObjectName.getInstance(name), attr.getName))
  }
}
