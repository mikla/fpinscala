package shapelessex.astronaut

import shapeless.ops.hlist
import shapeless._

case class EmployeeVer2(name: String, manager: Boolean)

trait Migration[A, B] {
  def apply(a: A): B
}

object syntax {

  implicit class MigrationOps[A](a: A) {
    def migrateTo[B](implicit m: Migration[A, B]): B = m.apply(a)
  }

  implicit def deletionMigration[A, B, ARepr <: HList, BRepr <: HList](
    implicit
    genA: LabelledGeneric.Aux[A, ARepr],
    genB: LabelledGeneric.Aux[B, BRepr],
    inter: hlist.Intersection.Aux[ARepr, BRepr, BRepr]
  ): Migration[A, B] = new Migration[A, B] {
    override def apply(a: A): B =
      genB.from(inter.apply(genA.to(a)))

}

}

object Chapter6CaseClassMigration extends App {
  import syntax._

  println(Employee("Name", 1, manager = true).migrateTo[EmployeeVer2])


}
