package circe_e

object CirceMeta extends App {

  import io.circe._, io.circe.parser._

  val rawJson: String = """
{
  "foo": "bar",
  "baz": 123,
  "list of stuff": [ 4, 5, 6 ]
}
"""

  val parseResult = parse(rawJson)

//  def traverseJsonTree(j: Json)

  println(parseResult.toOption.get.hcursor.keys)

}
