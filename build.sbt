name := "Functional Programming in Scala"

scalaVersion := "2.11.1"

resolvers ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
  "org.scalatest" % "scalatest_2.11" % "2.1.7" % "test",
  "com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT"
)