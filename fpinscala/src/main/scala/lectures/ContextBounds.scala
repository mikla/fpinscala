package lectures

import cats.Monoid
import cats.implicits._

object ContextBounds extends App {

  {
    // I fully don't get this construct [T: Zoned: DSTCheck] - how does it work?

    // Let's figure out WTF is this?

    case class Calculator[T : Monoid](value: T)

    // It's actually equivalent to:

    case class Calculator1[T](value: T)(implicit M: Monoid[T])

    // That's, basically it

    // At the moment of creation there must be available implicit instance of Monoid[T]

    Calculator(1)

    // When you have more than one bounds after : Like Calculator[T: Zoned: DSTCheck]
    // It means we are expanding this to
    // (implicit Z: Zoned[T], D: DSTCheck[T])

    // IMPORTANT-1: Note how many type parameters have Monoid, TimePoint, Zoned, DSTCheck

    // IMPORTANT-2: Traits can't have this Context Bounds, only abstract classes and case classes and defs
  }

  { // How to use it?

    case class Calculator[T : Monoid](value: T)

    // Monoid gives you two functions on type T:

    // def combine(x: A, y: A): A
    // def empty: A
    // You can go to definition of this on cats library.

    // combine on type Int it's just +
    // empty on type Int it's 0

    // combine on List - it's concatenation (++)
    // empty in List - it's empty list.

    // Ant etc.

    // Back to our Calculator example

    case class Calculator1[T : Monoid](value: T) {
      def calculate(value2: T): T = {
        // let's just `combine` two values... No rocket since
        // One way is to get our Monoid[T] instance by calling implicitly function, like this:

        val m = implicitly[Monoid[T]]

        // then we cam call `combine` on `m`

        m.combine(value2, value)

        // But it's inconvenient that you have to take this implicit manually

        // cat's provides you so called `syntax` for it.
        // you can call combine on `value` directly

        value.combine(value2)

        // Note how it's implemented, Jump to combine definition. (SemigroupSyntax)
      }

      // Lot's of these syntaxes for different standard type and Type classes are already in cats.
    }
  }

  { // Our own context bounds and syntax.
    // In NEVOS we have Zoned[T], TimePoint[T], DSTCheck[T]
    // All there typeclasses are custom, and we have syntax defined manually for each of them.

    // You may think for now, that Type Classes it's just an interfaces.
    // Just show you what operations you allowed to do on provided type T.
    // If you open Zoned or TimePoint definition you will see, that it's just trait with lot's of abstract functions.

    // Let's try to create our own Type class and convenient syntax for it.
    // Let's say to some type T we want some function that may gives us some human readable String

    trait HumanReadable[T] {
      def humanly(value: T): String
    }

    // this trait is just defined simple operation `humanly`, that knows how to convert some T to String (human
    // readable)

    // we can provide instances to known types

    implicit val intHumanReadable = new HumanReadable[Int] {
      override def humanly(value: Int): String = s"Int: value = $value"
    }

    // and etc.
    // But, in order to it we can't just call `humanly` on any Int

    // 1.humanly <---- COMPILATION ERROR!

    // we can either take out HumanReadable instance for Int via `implicitly` function
    // as it was shown above

    println {
      implicitly[HumanReadable[Int]].humanly(1) // Prints "Int: value = 1"
    }

    // But it's super ugly.
    // Therefore, there is a way to improve it.

    // We can extend any type T (remember Strings in Scala?), that has implicit HumanReadable instance by humanly functions.

    // I will show you one way of doing this
    // using implicit def, see Lesson 3.

    case class HumanlyOps[T](value: T)(implicit H: HumanReadable[T]) {
      def humanly: String = H.humanly(value)
    }

    implicit def humanlyExtenstion[A : HumanReadable](value: A) = HumanlyOps(value)

    // And now we can call humanly on Int

    // Note that case class accepts HumanReadable as implicit parameter
    // But implicit def has Context Bound

    // First it gives up better error message in case if HumanReadable[T] instance not found
    // Second, it allows us to call humanly on H, without implicitly[] call

    println {
      1.humanly
    }

  }

  // This Scala Type Classes stuff is actually goes way deeper, and has a lot of specific problems. (Existing only in
  // Scala). It's because Scala's Type Classes wasn't really designed properly, if they even had design :)

  // Lot's of different DSLs make using this approach.

  // Please, ask questions!

}
