package scalaz.validation

import scalaz.ValidationNel
import scalaz._
import Scalaz._

object TryValidation extends App {

  val bad = ""

  def parsePerson(in: String): ValidationNel[String, Person] = {
    val components = in.split(';').lift
    val name = components(0).fold("No name found".failureNel[String])(_.successNel[String])
    val date = components(1).fold("No date found".failureNel[String])(_.successNel[String])
    val address = components(2).fold("No address found".failureNel[String])(_.successNel[String])
    (name |@| date |@| address) { Person }
  }

  println(parsePerson(bad))

}
