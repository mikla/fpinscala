# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this repo is

A personal playground for functional programming in Scala. It's not an application: it's a collection of small, independent experiments and book/course exercises. Most files are standalone `object XxxApp extends App` (or `IOApp`) programs that demonstrate a single idea. The README maps the main packages to what inspired them (FP in Scala book → `fp_in_scala`, Type Astronaut's Guide → `shapelessex`, Functional Structures in Scala → `fsis`, FP to the Max → `fptomax`, Cats exercises → `catsex.exercises`, jdegoes' Functional Scala → `jdg.functionalscala`, Fix experiments → `fixpoint`).

## Build (sbt 1.13.0, Scala 2.13.18, JDK 25)

The project needs JDK 21+ (virtual threads), and `build.sbt` refuses to load on anything older. JDK 25 is pinned in `.sdkmanrc`, but the machine's default JDK is 17, so run `sdk env` in the repo first (or pass `sbt -java-home ~/.sdkman/candidates/java/25.0.4-tem`). Scala 2.13 officially supports JDK 25 from 2.13.17. If you bump Scala, bump kind-projector too: it's published per exact Scala version.

```bash
sbt compile                                   # root: aggregates only `common` + `fpinscala`
sbt monix/compile                             # other modules must be targeted explicitly
sbt fpinscala/test                            # the only module with tests (ScalaTest AnyFlatSpec + ScalaCheck)
sbt "fpinscala/testOnly fp_in_scala.datastructure.ListSpec"
sbt "fpinscala/runMain catsex.exercises.MonadApp"
sbt "catsEffect/runMain cats_effect.PingPongApp"
sbt scalafmtAll                               # formatting (.scalafmt.conf, maxColumn 120)
```

- sbt aliases in `build.sbt`: `c` (compile), `r` (reload), `rc` (reload + compile).
- `.sbtopts` sets a 4G heap, a very deep max stack trace, and Monix full stack tracing.
- `catsEffect` forks `run`: each app gets a fresh JVM, so `IOApp` behaves properly and virtual-thread scheduler system properties can be set in `main`.
- sbt 1.13 prints `sun.misc.Unsafe` / restricted-method warnings on JDK 25. They come from sbt's own jars and are harmless.

## Module layout (see `build.sbt`)

sbt project IDs aren't always the same as the directory names:

| sbt project | dir | notes |
|---|---|---|
| `common` | `common/` | shared models (`model.Employee`, `Location`, …) and helpers. No library deps |
| `fpinscala` | `fpinscala/` | the bulk of the experiments; depends on `common` and `macroo` |
| `macroo` | `macro/` | macro code (the name ends in "oo" because `macro` is a Scala keyword) |
| `catsEffect` | `cats-effect/` | Cats Effect 3 experiments; `cats_effect.jmm` has the Java Memory Model and virtual-thread examples |
| `monix` | `monix/` | Monix Task/Observable experiments |
| `performance` | `performance/` | JMH benchmarks (sbt-jmh). Currently doesn't compile: `CollectionSearch`/`ToIdMap` reference code that isn't in the repo |
| `dockerApp` | `docker-app/` | sbt-native-packager Docker image (`openjdk:11` base) |

Most modules share one large `commonDeps` bundle: cats, cats-free, kittens, shapeless, circe (plus generic-extras and derivation), ZIO 1.x, monocle 3, refined, pureconfig, enumeratum, spire, sttp4, and others. So a library is usually already available in any module that includes `commonDeps`. The compiler options are `-Ymacro-annotations` plus the kind-projector plugin, so `*` / `λ` type lambdas and `@Lenses`-style macro annotations work.

## JMH benchmarks

Run them from sbt inside the `performance` project. The `.md` files next to the benchmarks in `performance/src/main/scala/performance/jmh/` hold the commands and past results:

```
sbt "performance/jmh:run -i 3 -wi 3 -f1 -t1 .*CollectionSearch.*"
```

## Local infra

`docker-compose.yml` starts a 4-node MinIO cluster on ports 9001–9004 (user `local_minio` / password `local_minio123`). `fpinscala/src/main/scala/application/minio/MinioClientApp.scala` uses it.
