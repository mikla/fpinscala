lazy val commonSettings = Seq(
  organization := "com.fpinscala",
  version := "0.1.0",
  scalaVersion := "2.13.11"
)

lazy val compilerFlags = Seq(
  "-Ymacro-annotations"
)

resolvers ++= Seq(
  "Sonatype Releases".at("https://oss.sonatype.org/content/repositories/releases"),
  "Akka Snapshot Repository".at("https://repo.akka.io/snapshots/")
)

resolvers ++= Resolver.sonatypeOssRepos("releases")
resolvers += MavenCache("local-maven", file("/Users/user/.ivy2/"))

ThisBuild / libraryDependencySchemes ++= Seq(
  "io.circe" %% "circe-core" % VersionScheme.Always,
  "io.circe" %% "circe-generic" % VersionScheme.Always // IDEA fails to import project.
)

val shapelessVersion = "2.3.10"
val monixVersion = "3.4.1"
val enumeratumVersion = "1.7.2"
val pureConfigVersion = "0.17.4"
val catsVersion = "2.9.0"
val catsEffectVersion = "3.5.1"
val kittensVersion = "2.3.2"
val scalaCheckVersion = "1.17.0"
val scalaTestVersion = "3.2.3"
val spireVerison = "0.18.0"
val log4catsVersion = "2.6.0"
val circeVersion = "0.14.5"
val circeDerivatioinVersion = "0.13.0-M5"
val supertaggedVersion = "1.5"
val monocleVersion = "3.2.0"
val zioVersion = "1.0.12"
val dtcVersion = "2.6.0"
val typesafeConfigVersion = "1.4.2"
val refinedVersion = "0.11.0"
val sttpVersion = "4.0.0-M6"

lazy val compilerSettings = Seq(
  scalacOptions ++= compilerFlags
)

val catsEffectDeps = libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "org.typelevel" %% "log4cats-slf4j" % log4catsVersion
)

lazy val commonDeps = libraryDependencies ++= Seq(
  compilerPlugin(("org.typelevel" %% "kind-projector" % "0.13.2").cross(CrossVersion.full)),
  "ru.pavkin" %% "dtc-core" % dtcVersion,
  "com.chuusai" %% "shapeless" % shapelessVersion,
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.typesafe" % "config" % typesafeConfigVersion,
  "ch.qos.logback" % "logback-classic" % "1.4.8",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-derivation" % circeDerivatioinVersion,
  "io.circe" %% "circe-generic-extras" % "0.14.3",
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
  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion,
  "com.github.pureconfig" %% "pureconfig-enumeratum" % pureConfigVersion,
  "com.beachape" %% "enumeratum" % enumeratumVersion,
  "dev.optics" %% "monocle-core" % monocleVersion,
  "dev.optics" %% "monocle-macro" % monocleVersion,
  "eu.timepit" %% "refined" % refinedVersion,
  "com.softwaremill.sttp.client4" %% "core" % sttpVersion,
  "com.softwaremill.sttp.client4" %% "async-http-client-backend-cats" % sttpVersion,
  "com.softwaremill.sttp.client4" %% "circe" % sttpVersion,
  "io.minio" % "minio" % "8.5.8",
  "ai.catboost" % "catboost-prediction" % "1.2.2",
  "dev.optics" %% "monocle-law" % monocleVersion % "test",
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
  .settings(libraryDependencies ++= Seq("com.scylladb" % "java-driver-core" % "4.15.0.0"))
  .dependsOn(common, macroo)

lazy val dockerApp = (project in file("docker-app"))
  .enablePlugins(JavaAppPackaging)
  .settings(dockerBaseImage := "openjdk:11")
//  .settings {
//    Universal / javaOptions ++= {
//      val source = file(s"${sourceDirectory.value}/main/resources/app.jvmopts")
//      if (source.exists()) Utils.readJavaOptions(source) else sys.error(s"Could not find $source")
//    }
//  }
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

lazy val macroo = (project in file("macro"))
  .settings(commonSettings)
  .settings(compilerSettings: _*)
  .settings(commonDeps)
  .dependsOn(common)

lazy val monix = (project in file("monix"))
  .settings(commonSettings)
  .settings(compilerSettings: _*)
  .settings(commonDeps)
  .dependsOn(common)
  .settings(
    libraryDependencies ++= Seq(
      "io.monix" %% "monix-execution" % monixVersion,
      "io.monix" %% "monix-eval" % monixVersion,
      "io.monix" %% "monix" % monixVersion
    )
  )

lazy val catsEffect = (project in file("cats-effect"))
  .settings(commonSettings)
  .settings(compilerSettings: _*)
  .settings(commonDeps)
  .dependsOn(common)
  .settings(catsEffectDeps)

addCommandAlias("c", ";compile")
addCommandAlias("r", ";reload")
addCommandAlias("rc", ";reload;compile")

logLevel := Level.Debug
