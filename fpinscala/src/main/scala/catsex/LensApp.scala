package catsex

import cats.{Functor, Id}
import cats.data.Const
import shapelessex.astronaut.Employee
import cats.implicits._

import scala.language.higherKinds

trait Lens[S, A] {

  def get(s: S): A

  def set(s: S, a: A): S = modify(s)(_ => a)

  def modify(s: S)(f: A => A): S = modifyF[Id](s)(f)

  def modifyF[F[_]: Functor](s: S)(f: A => F[A]): F[S] =
    f(get(s)).map(a => set(s, a))

}

object LensApp extends App {

  val employeeNameUpdateLens = new Lens[Employee, String] {
    def get(s: Employee): String = s.name
  }

  println(employeeNameUpdateLens.modify(Employee("name", 1, true))(_ => "mikla"))

}