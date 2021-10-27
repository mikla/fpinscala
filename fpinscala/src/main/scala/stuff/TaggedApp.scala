package stuff

import supertagged.TaggedType

object TaggedApp extends App {

  object Width extends TaggedType[Int]
  type Width = Width.Type

  object Height extends TaggedType[String]
  type Height = Height.Type

  val width = Width @@ 5
  val height = Height @@ ""

}
