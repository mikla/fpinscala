package com.spoj

object LifeUniverse extends App {
  val iterator = io.Source.stdin.getLines
  iterator.foreach { line =>
    if (line != "42") println(line)
    else System.exit(0)
  }
}
