package macros

import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

object MacroApp extends App {
  def assert(cond: Boolean, msg: Any) = macro Asserts.assertImpl
}

object Asserts {
  def raise(msg: Any) = throw new AssertionError(msg)

  def assertImpl(c: Context)(cond: c.Expr[Boolean], msg: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._
    /*<[ if (!cond) raise(msg) ]>
    else
    <[ () ]>*/
    ???
  }
}