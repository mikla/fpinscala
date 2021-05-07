package catsex.free

import catsex.free.KVStore.{KVStore, _}

object KVStoreApp extends App {

  import KVStoreInterpreters._

  def program: KVStore[(Option[Int], Option[Int])] = for {
    _ <- put("wild-cats", 2)
    _ <- update[Int]("wild-cats", _ + 12)
    _ <- put("tame-cats", 10)
    wildCats <- get[Int]("wild-cats")
    _ <- delete("tame-cats")
    tameCats <- get[Int]("tame-cats")
  } yield (wildCats, tameCats)

  println(program.foldMap(impureCompiler))

}
