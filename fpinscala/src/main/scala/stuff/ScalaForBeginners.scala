package stuff
object ScalaForBeginners extends App {

  // val x = ???
  // if () else ()
  // while () {}

  // Types

  // Int
  // Long 2^62
  // Double / Float 3.455434
  // Boolean : True | False

  val x = 1

  val str = "hello"

  if (x == 1) println("1")
  else println("not 1")

  while (x > 2) {
    println("infinite loop")
  }

  // f: X => Y
  def inc(x: Int): Int = x + 1

  println(inc(4))

  // f: X => Y => Z

  def sum(x: Int, y: Int): Int = x + y

  println(sum(4, 5))

  // [1, 2, 3, 4, 5]
  // ("key" -> 4), ("key2" -> 3)

  val list = List(1, 2, 3, 4, 5)
  println(list(2))

  println(list.contains(10))

  case class User(
    id: String,
    email: String,
    name: String,
    isActive: Boolean
  )

  val users = List(
    User("1", "email1", "user1", isActive = true),
    User("2", "email2", "user2", isActive = true),
    User("3", "email3", "user3", isActive = false),
    User("4", "email4", "user4", isActive = true)
  )

  def sendEmail(email: String): Boolean = {
    if (email.contains("2")) true
    else false
  }

  users.foreach { user =>
    val sendEmailResult = sendEmail(user.email)
    if (sendEmailResult) println(s"Email sent to ${user.name}")
    else println(s"Email failed ${user.name}")
  }

    for {
    l <- List(1, 2)
  } sendEmail(l.toString)

  val external: Option[Boolean] = Some(true)

}
