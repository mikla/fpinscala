
object GenericProductUpdate extends App {

  case class &[L, R](left: L, right: R)

  implicit class AndOp[L](val left: L) {
    def &[R](right: R): L & R = new &(left, right)
  }

  trait ProductUpdater[P, A] {
    def apply(p: P, f: A => A): P
  }

  trait LowPriorityProductUpdater {
    implicit def noopValueUpdater[P, A]: ProductUpdater[P, A] = {
      new ProductUpdater[P, A] {
        def apply(p: P, f: A => A): P = p // keep as is
      }
    }
  }

  object ProductUpdater extends LowPriorityProductUpdater {
    implicit def simpleValueUpdater[A]: ProductUpdater[A, A] = {
      new ProductUpdater[A, A] {
        def apply(p: A, f: A => A): A = f(p)
      }
    }

    implicit def productUpdater[L, R, A](implicit leftUpdater: ProductUpdater[L, A],
                                         rightUpdater: ProductUpdater[R, A]): ProductUpdater[L & R, A] = {
      new ProductUpdater[L & R, A] {
        def apply(p: L & R, f: A => A): L & R = &(leftUpdater(p.left, f), rightUpdater(p.right, f))
      }
    }
  }

  def update[A, P](product: P)(f: A => A)(implicit updater: ProductUpdater[P, A]): P = updater(product, f)

  //////

  case class User(name: String, age: Int)

  val p: String & Int & User & String = "hello" & 123 & User("Elwood", 23) & "bye"

  val pUpd = update(p) { i: String =>
    i.toUpperCase
  }

  println(pUpd)

}
