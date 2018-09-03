package io

import cats.implicits._
import cats.effect.IO
import io.print._

import scala.io.StdIn

object DeleteFilesIOApp extends App {

  type FilePath = String

  def putStr(str: String): IO[Unit] = IO.pure(str.print())
  def putStrLn(str: String): IO[Unit] = IO.pure(println(str))
  def readLine(): IO[String] = IO.pure(StdIn.readLine())
  def getDirectoryContents(root: String): IO[List[FilePath]] =
    IO.pure(List("build.sbt", "readme.md"))

  def removeFile(filePath: FilePath): IO[Unit] = IO.pure(())

  def getFiles(pattern: String): IO[List[FilePath]] =
    getDirectoryContents(".").map(_.filter(_.contains(pattern)))

  def deleteFile(filePath: FilePath): IO[Unit] = for {
    _ <- putStrLn("Deleting file: " + filePath)
    _ <- removeFile(filePath)
  } yield ()

  def main1: IO[Unit] = for {
    _ <- putStr("Substring: ")
    p <- readLine()
    _ <- getFiles(p.trim).map(_.map(deleteFile).sequence)
  } yield ()

  main1.unsafeRunSync()

}
