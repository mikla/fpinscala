package catsex.free

import cats.{Id, ~>}

import scala.collection.mutable

object KVStoreInterpreters {

  def impureCompiler: KVStoreA ~> Id = new (KVStoreA ~> Id) {

    val kvs = mutable.Map.empty[String, Any]

    override def apply[A](fa: KVStoreA[A]): Id[A] =
      fa match {
        case Put(key, value) =>
          println(s"put $key->$value")
          kvs(key) = value

        case Get(key) =>
          println(s"get $key")
          kvs.get(key).map(_.asInstanceOf[A])
        case Delete(key) =>
          println(s"delete($key)")
          kvs.remove(key)
          ()
      }

  }

}
