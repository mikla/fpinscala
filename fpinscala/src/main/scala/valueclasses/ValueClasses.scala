package valueclasses

object ValueClasses extends App {

  class Foo(val i: Int) extends AnyVal

//  class Bar(val b: Foo) extends AnyVal waiting this in Dotty

}
