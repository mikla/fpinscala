lazy val commonSettings = Seq(
  organization := "com.fpinscala",
  version := "0.1.0",
  scalaVersion := "2.12.10"
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
val monixVersion = "3.1.0"
val pureConfigVersion = "0.12.1"
val catsVersion = "2.0.0"
val catsEffectVersion = "2.0.0"
val kittensVersion = "2.0.0"
val similacrumVersion = "0.19.0"
val scalaCheckVersion = "1.14.2"
val scalaTestVersion = "3.0.8"
val enumeratumVersion = "1.5.13"
val spireVerison = "0.16.2"
val log4catsVersion = "1.0.1"
val circeVersion = "0.12.3"
val supertaggedVersion = "1.4"
val monocleVersion = "2.0.0"
val zioVersion = "1.0.0-RC17"

lazy val compilerSettings = Seq(
  scalacOptions ++= compilerFlags
)

lazy val commonDeps = libraryDependencies ++= Seq(
  compilerPlugin("org.scalamacros" %% "paradise" % "2.1.1" cross CrossVersion.full),
  compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3" cross CrossVersion.binary),
  "com.chuusai" %% "shapeless" % shapelessVersion,
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.github.mpilquist" %% "simulacrum" % similacrumVersion,

  "org.typelevel" %% "cats-effect" % catsEffectVersion,

  //  "io.chrisdavenport" %% "log4cats-core" % log4catsVersion, // Only if you want to Support Any Backend
  "io.chrisdavenport" %% "log4cats-slf4j" % log4catsVersion,

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,

  "org.rudogma" %% "supertagged" % supertaggedVersion,

  "org.typelevel" %% "spire" % spireVerison,

  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-macros" % catsVersion,
  "org.typelevel" %% "cats-kernel" % catsVersion,
  "org.typelevel" %% "alleycats-core" % catsVersion,
  "org.typelevel" %% "cats-free" % catsVersion,

  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-streams" % zioVersion,
  "dev.zio" %% "zio-test" % zioVersion % "test",
  "dev.zio" %% "zio-test-sbt" % zioVersion % "test",

  "org.typelevel" %% "kittens" % kittensVersion,

  "io.monix" %% "monix-execution" % monixVersion,
  "io.monix" %% "monix-eval" % monixVersion,
  "io.monix" %% "monix" % monixVersion,

  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion,
  "com.github.pureconfig" %% "pureconfig-enumeratum" % pureConfigVersion,

  "com.beachape" %% "enumeratum" % enumeratumVersion,

  "com.github.julien-truffaut" %% "monocle-core" % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,

  "eu.timepit" %% "refined" % "0.9.10",

  // cassandra driver
  "com.outworkers" %% "phantom-dsl" % "2.42.0",

  "com.github.julien-truffaut" %% "monocle-law" % monocleVersion % "test",

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

lazy val performance = (project in file("performance"))
  .enablePlugins(JmhPlugin)
  .settings(compilerSettings: _*)
  .settings(commonSettings)
  .settings(commonDeps)
  .dependsOn(common)

lazy val fpinscalaRoot = (project in file("."))
  .settings(commonSettings)
  .aggregate(common, fpinscala)

addCommandAlias("c", ";compile")
addCommandAlias("r", ";reload")
addCommandAlias("rc", ";reload;compile")