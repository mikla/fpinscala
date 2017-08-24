package stuff

object MapValuesIteratorSOApp extends App {

  val initialIterator = Range(1, 4).toIterator

  val chainOfIterators = Range(0, 48000).foldRight(initialIterator) {
    case (_, acc) => acc.map(identity).filter(_ => true)
  }

  chainOfIterators.hasNext

}
