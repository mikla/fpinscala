package stuff

object Combinations extends App {

  val pairsBy2 = (1 to 12).toArray.combinations(2).toList.map(_.toList)

  println(pairsBy2.length)
  pairsBy2.foreach(println)

  val pairedGames = pairsBy2.combinations(2).toList
    .filter(pairs => pairs.flatMap(_.toSet).toSet.size == 4)

  println("total games: " + pairedGames.length)
  pairedGames.foreach { case p1 :: p2 :: Nil =>
    println(p1 + " vs. " + p2)
  }

}
