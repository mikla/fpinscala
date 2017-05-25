package stuff

import cats.kernel.Semigroup

trait ModelRW[R] {

  def update(value: R): R

}

class RootModel(auth: Option[String])
case class App1Model(appName: String, users: List[String], auth: Option[String]) extends RootModel(auth)
case class App2Model(appName: String, auth: Option[String]) extends RootModel(auth)

class AuthHandler[M <: RootModel](implicit U: RootModelUpdater[M]) extends ModelRW[M] {

  override def update(value: M): M = {
    // update auth and return back updated model
    U.set(value, Some("auth ok"))
  }

}

trait RootModelUpdater[T <: RootModel] {
  def set(t: T, auth: Option[String]): T
}


object RootModelHandlerApp extends App {

  val app1Model = App1Model("Scheduling", List.empty, None)

  implicit val app1ModelAuthUpdater = new RootModelUpdater[App1Model] {
    override def set(t: App1Model, auth: Option[String]): App1Model = t.copy(auth = auth)
  }

  val handler = new AuthHandler[App1Model]()

  println(handler.update(app1Model))


  Semigroup

}
