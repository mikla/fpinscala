package fp_in_scala.parser

import scala.language.higherKinds

class ParseError

trait Parsers[ParserError, Parser[+_]] { self =>

  def char(a: Char): Parser[Char]

  implicit def string(s: String): Parser[String]
  implicit def orString(s1: String, s2: String): Parser[String]
  implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]): ParserOps[String] = ParserOps(f(a))

  // run(listOfN(3, "ab" | "cad"))("ababcad") == Right("ababcad")
  def listOfN[A](n: Int, p: Parser[A]): List[Parser[A]]

  def or[A](s1: Parser[A], s2: Parser[A]): Parser[A]

  def run[A](p: Parser[A])(input: String): Either[ParseError, A]

  case class ParserOps[A](p: Parser[A]) {
    def |[B >: A](p2: Parser[B]): Parser[B] = self.or(p, p2)
    def or[B >: A](p2: => Parser[B]): Parser[B] = self.or(p, p2)
  }

}
