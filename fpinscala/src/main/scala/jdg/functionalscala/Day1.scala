package jdg.functionalscala

import java.time.LocalDate

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

object Day1 extends App {

  //
  // EXERCISE 19
  //
  // Using algebraic data types and smart constructors, make it impossible to
  // construct a `BankAccount` with an illegal (undefined) state in the
  // business domain. Note any limitations in your solution.
  // case class BankAccount(ownerId: String, balance: BigDecimal, accountType: String, openedDate: Long)

  case class OwnerId private (id: String)

  object OwnerId {
    def fromString(value: String): Option[OwnerId] =
      if (value.nonEmpty) Some(new OwnerId(value)) else None
    private def apply(id: String): OwnerId = new OwnerId(id)
  }

  sealed trait Currency

  case object EUR extends Currency

  case object USD extends Currency

  case class Balance(balance: Int Refined NonNegative, currency: Currency)

  sealed trait AccountType

  case object PersonalAccount extends AccountType

  case object BusinessAccount extends AccountType

  case class BankAccount(ownerId: OwnerId, balance: Balance, accountType: AccountType, openedDate: LocalDate)

  val balance = Balance(5, EUR)

  //
  // EXERCISE 2
  //
  // Convert the following non-function into a function.
  //
  def arrayUpdate1[A](arr: Array[A], i: Int, f: A => A): Unit =
    arr.update(i, f(arr(i)))

  def arrayUpdate2[A](arr: Array[A], i: Int, f: A => A): Option[Array[A]] =
    if (i > arr.length - 1 || i < 0) None
    else {
      val copied: Array[A] = arr.clone()
      copied.update(i, f(arr(i)))
      Some(copied)
    }

  //
  // EXERCISE 6
  //
  // Convert the following non-function into function.
  //
  //  def head1[A](as: List[A]): A = {
  //    if (as.length == 0) println("Oh no, it's impossible!!!")
  //    as.head
  //}

  def head2[A, B](as: List[A])(ifPresent: A => B, ifEmpty: => B): B =
    as.headOption.fold(ifEmpty)(ifPresent)

  //
  // EXERCISE 7
  //
  // Convert the following non-function into a function.
  //

  // Note: instead of doing something - describe.

  trait Account

  trait Processor {
    def charge(account: Account, amount: Double): Unit
  }

  final case class Coffee() {
    final val price = 3.14
  }

  def buyCoffee1(processor: Processor, account: Account): Coffee = {
    val coffee = Coffee()
    processor.charge(account, coffee.price)
    coffee
  }

  final case class Charge[A](account: Account, amount: Double, value: A)

  def buyCoffee2(account: Account): Charge[Coffee] =
    Charge(new Account {}, 1.0d, Coffee())

  //
  // EXERCISE 11
  //
  // Rewrite the following non-function `printer1` into a pure function, which
  // could be used by pure or impure code.
  //
  def printer2[A](println: String => A, combine: (A, A) => A): A =
    List(
      "Welcome to the help page!",
      "To list commands, type `commands`.",
      "For help on a command, type `help <command>`",
      "For help on a command, type `help <command>`",
      "To exit the help page, type `exit`."
    ).map(println).reduce(combine)

  printer2[Unit](println(_), (_, _) => ())

  //
  // EXERCISE 12
  //
  // Create a purely-functional drawing library that is equivalent in
  // expressive power to the following procedural library.
  //

  case class Canvas(x: Int, y: Int, pixels: List[List[Boolean]])

  type DrawCommand = Canvas => Canvas
  val goLeft: DrawCommand = (c: Canvas) => c.copy(x = c.x - 1)
  val goRight: DrawCommand = (c: Canvas) => c.copy(x = c.y + 1)
  val goUp: DrawCommand = (c: Canvas) => c.copy(y = c.y + 1)
  val goDown: DrawCommand = (c: Canvas) => c.copy(y = c.y - 1)
  val draw: DrawCommand = (c: Canvas) => ???

  goUp.andThen(goDown).andThen(goLeft)

  // Or we could introduce sum type for commands
  // sealed trait Command

  val add: Int => (Int => Int) = (x: Int) => (y: Int) => x + y

  // Identity is polymorphic
  def id[A](a: A): A = a

  def either[C, A, B](f: A => C, g: B => C): Either[A, B] => C = {
    case Left(a) => f(a)
    case Right(b) => g(b)
  }

  def either1[C, A, B](f: A => C, g: B => C): Either[A, B] => C = _.fold(f, g)

  def uneither[C, A, B](h: Either[A, B] => C): (A => C, B => C) =
    ((a: A) => h(Left(a)), (b: B) => h(Right(b)))

  // TODO
  // Use bimap
  // Use either
  def distRight[C, A, B]: Either[(A, C), (B, C)] => (Either[A, B], C) =
    ???

  // TODO
  def distLeft[C, A, B]: ((Either[A, B], C)) => Either[(A, C), (B, C)] =
    ???

  type ParserSimple[+A] = String => Option[(String, A)]

  def combine[A, B](p1: ParserSimple[A], p2: ParserSimple[B]): ParserSimple[(A, B)] = (input: String) =>
    p1(input).flatMap {
      case (input, a) =>
        p2(input).map {
          case (input, b) => (input, (a, b))
        }
    }

  // classic definition of parser combinator

  final case class Parser[+E, +A](run: String => Either[E, (String, A)])

  object Parser {
    final def fail[E](e: E): Parser[E, Nothing] =
      Parser(_ => Left(e))

    final def succeed[A](a: => A): Parser[Nothing, A] =
      Parser(input => Right((input, a)))

    final def char: Parser[Unit, Char] =
      Parser(input =>
        if (input.length == 0) Left(())
        else Right((input.drop(1), input.charAt(0))))
  }

  def alt[E1, E2, A, B](l: Parser[E1, A], r: E1 => Parser[E2, B]): Parser[(E1, E2), Either[A, B]] =
    Parser[(E1, E2), Either[A, B]] { (input: String) =>
      l.run(input) match {
        case Left(e1) =>
          r(e1).run(input) match {
            case Left(e2) =>
              Left((e1, e2))
            case Right((input, b)) =>
              ???
          }
        case Right((input, a)) =>
          ???
      }
    }

  //
  // EXERCISE 16
  //
  // Implement the function `groupBy1`.
  //
  val TestData =
    "poweroutage;2018-09-20;level=20" :: Nil
  val ByDate: String => String =
    (data: String) => data.split(";")(1)
  val Reducer: (String, List[String]) => String =
    (date, events) =>
      "On date " +
        date + ", there were " +
        events.length + " power outages"
  val ExpectedResults =
    Map(
      "2018-09-20" ->
        "On date 2018-09-20, there were 1 power outages"
    )
  def groupBy1(
    events: List[String],
    by: String => String
  )(reducer: (String, List[String]) => String): Map[String, String] = ???

  groupBy1(TestData, ByDate)(Reducer) == ExpectedResults

  //
  // EXERCISE 17
  //
  // Make the function `groupBy1` as polymorphic as possible and implement
  // the polymorphic function. Compare to the original.
  //
  object groupBy2 {
    def apply[Event, Key, C](
      events: List[Event],
      by: Event => Key
    )(reducer: (Key, List[Event]) => C): Map[Key, C] =
      events.groupBy(by).map { case (key, events) =>
        key -> reducer(key, events)
      }
  }

}
