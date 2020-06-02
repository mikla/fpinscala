package stuff

import scala.annotation.StaticAnnotation
import scala.reflect.macros.whitebox
import scala.language.experimental.macros

class AutoMenu() extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro AutoMenuImpl.impl
}

object AutoMenuImpl {

  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    def extractCaseClassesParts(classDecl: ClassDef) = classDecl match {
      case q"case class $className(..$fields) extends ..$parents { ..$body }" =>
        (className, fields, parents, body)
    }

    def modifiedDeclaration(classDecl: ClassDef) = {
      val (className, fields, parents, body) = extractCaseClassesParts(classDecl)

      val params = fields.asInstanceOf[List[ValDef]] map { p => p.duplicate}

      c.Expr[Any](
        q"""
        case class $className ( ..$params ) extends ..$parents {
          ..$body
          override def setUserMenuOpened(opened: Boolean): PageState = copy(userMenuOpened = opened)
          override def swapMobileMenuOpened: PageState = copy(mobileMenuOpened = !mobileMenuOpened)
        }
      """
      )
    }

    annottees.map(_.tree).toList match {
      case (classDecl: ClassDef) :: Nil => modifiedDeclaration(classDecl)
      case _ => c.abort(c.enclosingPosition, "Invalid annottee")
    }

  }
}