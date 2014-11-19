import scala.collection.mutable.ArrayBuffer

object Main extends App {

  abstract class IntQueue {
    def get(): Int
    def put(x: Int)
  }

  class BasicIntQueue extends IntQueue {
    private val buf = new ArrayBuffer[Int]()
    override def get(): Int = buf.remove(0)
    override def put(x: Int): Unit = { println("calling concrete"); buf += x}
  }

  trait Doubling extends IntQueue {
    abstract override def put(x: Int) { println("calling doubling"); super.put(x * 2) }
  }

  trait Incrementing extends IntQueue {
    abstract override def put(x: Int) { println("calling incrementing"); super.put(x + 1) }
  }

  val q = new BasicIntQueue with Incrementing with Doubling
  q.put(2)
  println(q.get())

  def maxList[T <% Ordered[T]](elements: List[T]): T =
    elements match {
      case List() =>
        throw new IllegalArgumentException("empty list!")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxList(rest) // (orderer) is implicit
        if (x > maxRest) x // orderer(x) is implicit
        else maxRest
    }

  println(maxList(List(1, 2, 4)))

  List(1, 2, 3).view.map(_ + 1)
  List(2, 3, 4).map(_ + 1)

  // Extractors!

  object Email extends Function2[String, String, String] {
    override def apply(v1: String, v2: String): String = v1 + "@" + v2
    def unapply(email: String): Option[(String, String)] = {
      val parts = email split "@"
      if (parts.length == 2) Some(parts(0), parts(1)) else None
    }
  }

  object UpperCase {
    def unapply(s: String): Boolean = s == s
  }

  object StartsWithM {
    def unapply(s: String): Boolean = s.startsWith("m")
  }

  "mikla@gmail.com" match {
    case Email(v1 @ UpperCase(), v2) => println(v1)
    case _ => println("")
  }

}
