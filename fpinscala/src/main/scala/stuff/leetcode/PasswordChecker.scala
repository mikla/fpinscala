package stuff.leetcode

object PasswordChecker extends App {

  object Solution {

    type Rule = String => Boolean

    val minCharsRule: Rule = s => s.length >= 6
    val maxCharsRule: Rule = _.length <= 20
    val oneLowerCase: Rule = s => "([a-z]+)".r.findFirstIn(s).isDefined
    val oneUpperCase: Rule = s => "([A-Z]+)".r.findFirstIn(s).isDefined
    val oneDigit: Rule = s => "([0-9]+)".r.findFirstIn(s).isDefined
    val repeatedRule: Rule = s => s.sliding(3, 3).toList.exists(s => s.forall(_ == s.head) && s.length == 3)

    val AllRules = List(
      minCharsRule,
      maxCharsRule,
      oneLowerCase,
      oneUpperCase,
      oneDigit,
      repeatedRule
    )

    def strongPasswordChecker(password: String): Int = {
      println(AllRules.map(_ (password)))

      AllRules.map(_ (password)).count(_ == false)

    }

  }

  println(Solution.strongPasswordChecker("a"))

}
