package shapelessex

import model.poly.{Level1, Level2, Outer, ToChange}

object PolyHirarchyConverterApp extends App {

  val origibal = Outer(
    Some(ToChange("original1")),
    Level1(ToChange("original2"), "string", Level2(List(ToChange("inlist1"), ToChange("inlist2")), 0)),
    b = false
  )

  println(implicitly[Converter[Outer]].polyConvert(origibal))

}
