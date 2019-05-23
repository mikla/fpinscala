package stuff.hackerrank

object RansomNoteApp extends App {

  def checkMagazine(magazine: Array[String], note: Array[String]) {
    val groupedMagazine = magazine.groupBy(identity)

    val res = note.groupBy(identity).map { case (word, words) =>
      groupedMagazine.get(word).exists(_.length >= words.length)
    }.forall(_ == true)

    if (res) println("Yes") else println("No")
  }

  checkMagazine("ive got a lovely bunch of coconuts".split(" "), "ive got some coconuts".split(" "))
  checkMagazine("give me one grand today night".split(" "), "give one grand today".split(" "))
  checkMagazine("two times three is not four".split(" "), "two times two is four".split(" "))
  checkMagazine("apgo clm w lxkvg mwz elo bg elo lxkvg elo apgo apgo w elo bg".split(" "), "elo lxkvg bg mwz clm w".split(" "))

}
