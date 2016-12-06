package shapelessex.application

import shapeless.labelled.FieldType
import shapeless.{:+:, ::, CNil, Coproduct, HList, HNil, Inl, Inr, Lazy, Witness}

trait Info[T] {
  def getInfo(t: T): Map[String, String]
}

trait Show[T] {
  def show(t: T): String
}

object Show {
  implicit def showAny[T]: Show[T] = new Show[T] {
    override def show(t: T): String = t.toString
  }
}

object Info extends Low {
  implicit def infoHNil: Info[HNil] = new Info[HNil] {
    override def getInfo(t: HNil): Map[String, String] = Map.empty
  }

  implicit def infoHCons[K <: Symbol, H, T <: HList](
    implicit key: Witness.Aux[K],
    sh: Lazy[Show[H]],
    st: Lazy[Info[T]]
  ): Info[FieldType[K, H] :: T] = new Info[FieldType[K, H] :: T] {

    override def getInfo(t: FieldType[K, H] :: T): Map[String, String] = {
      val head = Map(key.value.name -> sh.value.show(t.head)) // deal with toString()
      val tail = st.value.getInfo(t.tail)
      head ++ tail
    }
  }

  implicit def infoCNil: Info[CNil] = new Info[CNil] {
    override def getInfo(t: CNil): Map[String, String] = t.impossible
  }

  implicit def infoCCons[H, T <: Coproduct](
    sh: Info[H],
    st: Info[T]
  ): Info[H :+: T] = new Info[H :+: T] {
    override def getInfo(t: H :+: T): Map[String, String] = t match {
      case Inl(l) => sh.getInfo(l)
      case Inr(r) => st.getInfo(r)
    }
  }

}

trait Low {
  //  implicit def infoGeneric[T, Repr](
  //    implicit gen: LabelledGeneric.Aux[T, Repr],
  //    sg: Lazy[Info[Repr]]): Info[T] =
  //    new Info[T] {
  //      override def getInfo(t: T): Map[String, String] = sg.value.getInfo(gen.to(t))
  //    }
}

object LabelledGenericApp extends App {

  //  println(implicitly[Info[UserId]].getInfo(UserId.generate))
  //  println(implicitly[Info[ScheduleGroupId]].getInfo(ScheduleGroupId.generate))

  // co-products
  implicit val infoUserId: Info[UserId] = null

  implicit val strInfo: Info[String] = new Info[String] {
    override def getInfo(t: String): Map[String, String] = Map("str" -> t)
  }

  implicitly[Info[CNil]]

//  println(implicitly[Info[UserId :+: CNil]])

  //.getInfo(Coproduct(UserId.generate)))

//  def getEventInfo[T <: SchedulingEvent](event: T)(implicit I: Info[T]) =
//    I.getInfo(event)

  //  getEventInfo(scheduleAssignmentAdded)

  type IntOrStringOrBool = Int :+: String :+: Boolean :+: CNil

  val i = 43
  val icpr = Coproduct[IntOrStringOrBool](i)

}
