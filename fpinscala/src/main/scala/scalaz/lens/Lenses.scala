package scalaz.lens

import scalaz.Lens

object Lenses extends App {

  case class User(name: String, phone: String)

  def maskPhone(ph: String) = ph + "*"

  val maskPhoneL: Lens[User, String] = Lens.lensu((u, newPhone) => u.copy(phone = maskPhone(u.phone)), _.phone)

  val user = User("mikla", "+375 29 299 0303")

  println(maskPhoneL.set(user, ""))

}
