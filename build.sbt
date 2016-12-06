lazy val commonSettings = Seq(
  organization := "com.fpinscala",
  version := "0.1.0",
  scalaVersion := "2.11.8"
)

resolvers ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)

lazy val scalaReflect = Def.setting {
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
}

lazy val commonDeps = libraryDependencies ++= Seq(
  compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full),
  "org.scalaz" %% "scalaz-core" % "7.1.0",
  "org.scalaz" %% "scalaz-concurrent" % "7.1.0",
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
  "org.scalatest" % "scalatest_2.11" % "2.1.7" % "test",
  "io.reactivex" %% "rxscala" % "0.24.1",
  "com.ning" % "async-http-client" % "1.9.24",
  "org.jsoup" % "jsoup" % "1.8.1",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.github.mpilquist" %% "simulacrum" % "0.4.0",
  "org.typelevel" %% "cats" % "0.6.0"
)

lazy val common = (project in file("common"))
  .settings(commonSettings)

lazy val fpinscala = (project in file("fpinscala"))
  .settings(commonSettings)
  .settings(commonDeps)
  .dependsOn(common)

lazy val root = (project in file("."))
  .settings(commonSettings)
  .aggregate(common, fpinscala)