name := "Functional Programming in Scala"

scalaVersion := "2.11.5"

resolvers ++= Seq(
  //  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.0",
  "org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
  "org.scalatest" % "scalatest_2.11" % "2.1.7" % "test"
)