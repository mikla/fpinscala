lazy val commonSettings = Seq(
  organization := "com.fpinscala",
  version := "0.1.0",
  scalaVersion := "2.13.5"
)

lazy val compilerFlags = Seq(
  "-Ymacro-annotations",
  "-Wunused"
)

resolvers ++= Seq(
  "Sonatype Releases".at("https://oss.sonatype.org/content/repositories/releases"),
  "Akka Snapshot Repository".at("https://repo.akka.io/snapshots/")
)

resolvers += Resolver.sonatypeRepo("releases")

val shapelessVersion = "2.3.5"
val monixVersion = "3.3.0"
val pureConfigVersion = "0.12.3"
val catsVersion = "2.3.1"
val catsEffectVersion = "2.3.1"
val kittensVersion = "2.2.1"
val similacrumVersion = "1.0.1"
val scalaCheckVersion = "1.15.2"
val scalaTestVersion = "3.2.3"
val enumeratumVersion = "1.6.1"
val spireVerison = "0.17.0"
val log4catsVersion = "1.1.1"
val circeVersion = "0.13.0"
val circeDerivatioinVersion = "0.13.0-SNAPSHOT"
val supertaggedVersion = "1.5"
val monocleVersion = "2.1.0"
val zioVersion = "1.0.4"
val dtcVersion = "2.4.0"
val typesafeConfigVersion = "1.4.1"

lazy val compilerSettings = Seq(
  scalacOptions ++= compilerFlags
)

lazy val commonDeps = libraryDependencies ++= Seq(
  compilerPlugin(("org.typelevel" %% "kind-projector" % "0.11.3").cross(CrossVersion.full)),
  "ru.pavkin" %% "dtc-core" % dtcVersion,
  "com.chuusai" %% "shapeless" % shapelessVersion,
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.typelevel" %% "simulacrum" % similacrumVersion,
  "com.typesafe" % "config" % typesafeConfigVersion,
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "io.chrisdavenport" %% "log4cats-slf4j" % log4catsVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-derivation" % circeDerivatioinVersion,
  "io.circe" %% "circe-generic-extras" % "0.13.0",
  "io.circe" %% "circe-parser" % circeVersion,
  "org.rudogma" %% "supertagged" % supertaggedVersion,
  "org.typelevel" %% "spire" % spireVerison,
  "org.typelevel" %% "cats-core" % catsVersion,
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
  "eu.timepit" %% "refined" % "0.9.20",
  // cassandra driver
  "com.outworkers" %% "phantom-dsl" % "2.59.0",
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
  .dependsOn(common, macroo)

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

lazy val macroo = (project in file("macro"))
  .settings(commonSettings)
  .settings(compilerSettings: _*)
  .settings(commonDeps)
  .dependsOn(common)

addCommandAlias("c", ";compile")
addCommandAlias("r", ";reload")
addCommandAlias("rc", ";reload;compile")

logLevel := Level.Debug
