package catsex.free

import cats.free.Free

trait KVStoreA[A]
case class Put[T](key: String, value: T) extends KVStoreA[Unit]
case class Get[T](key: String) extends KVStoreA[Option[T]]
case class Delete(key: String) extends KVStoreA[Unit]

object KVStore {
  import cats.free.Free.liftF

  type KVStore[A] = Free[KVStoreA, A]

  def put[T](key: String, value: T): KVStore[Unit] =
    liftF[KVStoreA, Unit](Put[T](key, value))

  def get[T](key: String): KVStore[Option[T]] =
    liftF[KVStoreA, Option[T]](Get(key))

  def delete(key: String): KVStore[Unit] =
    liftF[KVStoreA, Unit](Delete(key))

  def update[T](key: String, updater: T => T): KVStore[Unit] =
    for {
      valueOpt <- get[T](key)
      _ <- valueOpt.map(value => put(key, updater(value))).getOrElse(Free.pure())
    } yield ()

}
