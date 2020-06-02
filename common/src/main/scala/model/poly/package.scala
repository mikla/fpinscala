package model

package object poly {

  case class ToChange(value: String)
  case class Level2(many: List[ToChange], i: Int)
  case class Level1(change: ToChange, s: String, level2: Level2)
  case class Outer(optChange: Option[ToChange], level1: Level1, b: Boolean)

}
