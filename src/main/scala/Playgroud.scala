import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

object Playgroud extends App {

  def swap(arr: Array[Any]) = {
    arr match {
      case Array(x, y, _*) => arr(0) = y; arr(1) = x; arr
      case _ => arr
    }
  }

  case class UserRole(company: Option[String])

  case class RoleRecord(applicationId: String, roleId: String, companyId: Option[String])

  val roles: Seq[RoleRecord] = Seq(
    RoleRecord("salary", "b2b.manager", Some("XA")),
    RoleRecord("salary", "b2b.admin", Some("XA")),
    RoleRecord("salary", "b2b.operator", Some("XA")),
    RoleRecord("salary", "b2b.admin", Some("XS")),
    RoleRecord("salary", "b2b.operator", None),
    RoleRecord("ibul", "ibul.admin", Some("XM"))
  )

  roles.groupBy(_.applicationId).foreach {
    case (applicationId, appRoles) =>
      println("==========")
      println(s"creating application $applicationId")
      appRoles.groupBy(_.roleId).foreach { case (roleId, roleRecords) =>
        println(s"creating role $applicationId, $roleId")

        roleRecords.view.map(_.companyId).flatten.foreach { companyId =>
          println(s"creating resource $applicationId, $roleId, $companyId")
        }
      }

  }


  def f1(a: Int, b: String, c: String) = ???
  val f2: (Int, String) => Nothing = f1(_, _, "")

}

trait X {
  type Foo
}


