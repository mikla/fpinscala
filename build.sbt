lazy val commonSettings = Seq(
  organization := "com.fpinscala",
  version := "0.1.0",
  scalaVersion := "2.12.4"
)

lazy val compilerFlags = Seq(
  "-Ypartial-unification",
  "-Ywarn-infer-any"
)

resolvers ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)

resolvers += Resolver.sonatypeRepo("releases")

val shapelessVersion = "2.3.3"
val monixVersion = "3.0.0-RC1"
val pureConfigVersion = "0.9.0"
val catsVersion = "1.1.0"
val catsEffectVersion = "1.0.0-RC"
val kittensVersion = "1.0.0-M9"
val similacrumVersion = "0.12.0"
val scalaCheckVersion = "1.13.4"
val scalaTestVersion = "3.0.5"
val enumeratumVersion = "1.5.12"
val spireVerison = "0.14.1"

lazy val compilerSettings = Seq(
  scalacOptions ++= compilerFlags
)

lazy val commonDeps = libraryDependencies ++= Seq(
  compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full),
  compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3" cross CrossVersion.binary),
  "com.chuusai" %% "shapeless" % shapelessVersion,
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.github.mpilquist" %% "simulacrum" % similacrumVersion,

  "org.typelevel" %% "cats-effect" % catsEffectVersion,

  "org.typelevel" %% "spire" % spireVerison,

  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-free" % catsVersion,

  "org.typelevel" %% "kittens" % kittensVersion,

  "io.monix" %% "monix-execution" % monixVersion,
  "io.monix" %% "monix-eval" % monixVersion,
  "io.monix" %% "monix" % monixVersion,

  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion,
  "com.github.pureconfig" %% "pureconfig-enumeratum" % pureConfigVersion,

  "com.beachape" %% "enumeratum" % enumeratumVersion,

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