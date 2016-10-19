name := "Functional Programming in Scala"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)

lazy val commonSettings = Seq(
  scalaVersion := "2.11.7"
)

lazy val scalaReflect = Def.setting {
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
}

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.0",
  "org.scalaz" %% "scalaz-concurrent" % "7.1.0",
  "com.chuusai" %% "shapeless" % "2.3.2",
//  "org.typelevel" %% "scalaz-contrib-210" % "0.2",
  "org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
  "org.scalatest" % "scalatest_2.11" % "2.1.7" % "test",
  "io.reactivex" %% "rxscala" % "0.24.1",
  "com.typesafe.akka" %% "akka-actor" % "2.3-SNAPSHOT",
  "com.ning" % "async-http-client" % "1.9.24",
  "org.jsoup" % "jsoup" % "1.8.1",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.github.mpilquist" %% "simulacrum" % "0.4.0",
  "org.typelevel" %% "cats" % "0.6.0"
)

lazy val macroSub = (project in file("macro")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies += scalaReflect.value
  )

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full)