name := "Functional Programming in Scala"

scalaVersion := "2.11.5"

resolvers ++= Seq(
  //  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.0",
  "org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
  "org.scalatest" % "scalatest_2.11" % "2.1.7" % "test",
  "com.chuusai" %% "shapeless" % "2.1.0",
  "io.reactivex" %% "rxscala" % "0.24.1",
  "com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT",
  "com.ning" % "async-http-client" % "1.9.24",
  "org.jsoup" % "jsoup" % "1.8.1"
)