package stuff

object TypeInfApp extends App {

  trait Printable[A] {
    def format(a: A): String
  }

  object PrintableInstances {
    implicit val stringPrintable = new Printable[String] {
      def format(input: String): String = input
    }

    implicit val intPrintable = new Printable[Int] {
      def format(input: Int): String = input.toString
    }
  }

  object Printable {
    def format[A](input: A)(implicit p: Printable[A]): String =
      p.format(input)

    def print[A](input: A)(implicit p: Printable[A]) = println(format(input))
  }

  object PrintableSyntax {
    //  import PrintableInstances.stringPrintable

    implicit class PrintableOps[A](value: A) {

      def format(implicit p: Printable[A]): String =
        p.format(value)

      def print_(implicit p: Printable[A]): Unit =
        println(format(p))
    }

  }

  object Main extends App {

    import PrintableInstances._
    import PrintableSyntax._
    import Printable._

    implicitly[Printable[String]]

    //  Printable.print("aaaaaaaaaaa")
    "asddd".print_

    "asdasd".format(stringPrintable)
  }

}
