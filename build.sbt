lazy val commonSettings = Seq(
  organization := "com.fpinscala",
  version := "0.1.0",
  scalaVersion := "2.12.3"
)

lazy val compilerFlags = Seq(
  "-Ypartial-unification"
)

resolvers ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)

resolvers += Resolver.sonatypeRepo("releases")

val shapelessVersion = "2.3.2"
val monixVersion = "2.3.0"
val catsVersion = "0.9.0"
val kittensVersion = "1.0.0-M9"
val similacrumVersion = "0.10.0"
val scalaCheckVersion = "1.11.4"
val scalaTestVersion = "3.0.1"

lazy val compilerSettings = Seq(
  scalacOptions ++= compilerFlags
)

lazy val commonDeps = libraryDependencies ++= Seq(
  compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full),
  compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3" cross CrossVersion.binary),
  "com.chuusai" %% "shapeless" % shapelessVersion,
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.github.mpilquist" %% "simulacrum" % similacrumVersion,
  "org.typelevel" %% "cats" % catsVersion,
  "org.typelevel" %% "kittens" % kittensVersion,
  "io.monix" %% "monix-execution" % monixVersion,
  "io.monix" %% "monix-eval" % monixVersion,
  "io.monix" %% "monix-cats" % monixVersion,
  "io.monix" %% "monix" % monixVersion,
  "org.scalacheck" %% "scalacheck" % scalaCheckVersion % "test",
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
)

lazy val common = (project in file("common"))
  .settings(compilerSettings: _*)
  .settings(commonSettings)

lazy val fpinscala = (project in file("fpinscala"))
  .settings(compilerSettings: _*)
  .settings(commonSettings)
  .settings(commonDeps)
  .dependsOn(common)

lazy val dependentTypes = (project in file("dependent-types"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Seq())

lazy val fpinscalaRoot = (project in file("."))
  .settings(commonSettings)
  .aggregate(common, fpinscala)