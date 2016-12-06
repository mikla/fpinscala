package shapelessex.application

import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr}

/**
  * Задача: у нас есть какой-то `case class` и мы хотим получить из него поле типа `UserId`,
  * если таковое там, конечно, присутствует.
  *
  * Для этого определим type class `UserIdGetter[T]`, который будет уметь нам доставать UserId поле из кейс класса.
  *
  * Далее, у шейплеса есть `Generic`, который умеет из кейс класса делать нам HList и обратно. (Generic.to & Generic.from)
  *
  * Все эти implicit defs, которые у нас есть - это этакие правила, их еще называют аксиомами почему-то, по которым
  * компилятор может вывести нам инстанс тайп класса для нужного нам типа.
  *
  * Ок, теперь по-порядку:
  * Изначально, мы требуем имплиситно инстанс `UserIdGetter[PermissionsChanged] (1)`.
  * Компилятор начинает шуршить наши имлиситные дефы и пытается понять, что нам может дать `UserIdGetter[PermissionsChanged]`.
  * Приходим в `genericUserIdGetter` у которого 2 тайп параметра (T - это наш кейс класс, Repr - это его HList предстваление).
  * Для нашего `PermissionsChanged` Repr = String :: UserId :: HNil. И тут же мы трубуем имплиситно инстанс
  * `UserIdGetter[String :: UserId :: HNil]`.
  * Опять шуршим наши имлписитные дефы: `hConsUserId` и `hNilUserId` нам не подходят, спускаемся в `LowerPriorityImplicits`.
  * Ага, супер, `hCons` нам может дать `UserIdGetter[String :: UserId :: HNil]`,
  * но он имплиситно хочет `UserIdGetter[UserId :: HNil]`,
  * well, ok, возвращаемся обратно в компаньон и смотрим, что `hConsUserId` может нам дать инстанс
  * UserIdGetter[UserId :: HNil], но в свою очередь, он хочет инстанс для хвоста UserIdGetter[HNil],
  * который нам даст `hNilUserId`.
  * Собственно на этом весь вывод и заканчивается.
  *
  * Понять цепочку имплиситных выводов можно с помощью идеи.
  * Он, конечно, плохо работает если мы просто на (1) попробуем попросить implicit parameters (Shift - CMD + P) из-за
  * магических макросов Generic-а, но если закоменитить `genericUserIdGetter`
  * и вызвать implicit parameters на строке `implicitly[UserIdGetter[String :: UserId :: HNil]]`, то он покажет нам
  * hCons -> hConsUserId -> hNilUserId
  *
  * Ok, пойдем дальше, это отлично работает когда мы имеем инстанс конкретного класса, но что если мы на входе имеем
  * Event и хотим взять из него UserId, если он там есть? (2) не скомпилируется...
  *
  *
  */
object ExtractFieldByTypeApp extends App {

  trait UserIdGetter[T] {
    def userId(t: T): Option[UserId]
  }

  object UserIdGetter extends LowerPriorityImplicits {

    implicit val hNilUserId = new UserIdGetter[HNil] {
      override def userId(t: HNil): Option[UserId] = None
    }

    implicit def hConsUserId[T <: HList](implicit A: UserIdGetter[T]) = new UserIdGetter[UserId :: T] {
      override def userId(t: UserId :: T): Option[UserId] = Some(t.head)
    }

    implicit def genericUserIdGetter[T, Repr](implicit G: Generic.Aux[T, Repr], A: UserIdGetter[Repr]) = new UserIdGetter[T] {
      override def userId(t: T): Option[UserId] = A.userId(G.to(t))
    }

  }

  trait LowerPriorityImplicits {

    implicit def hCons[H, T <: HList](implicit A: UserIdGetter[T]) = new UserIdGetter[H :: T] {
      override def userId(t: H :: T): Option[UserId] = A.userId(t.tail)
    }

  }

  // Usage

  val permChangedEvent = PermissionsChanged("name", UserId("1"))
  val locationCreatedEvent = LocationCreated("Malta")
  val allEvents: Seq[Event] = Seq(permChangedEvent, locationCreatedEvent)

  val permChangedUserIdGetter = implicitly[UserIdGetter[PermissionsChanged]] // (1)

  //  val permChangedUserIdGetterHl = implicitly[UserIdGetter[String :: UserId :: HNil]] // (1)

  println(permChangedUserIdGetter.userId(permChangedEvent))

  println(implicitly[UserIdGetter[LocationCreated]].userId(locationCreatedEvent))

  // filtering
  case class Filter(userId: UserId)

  def filterByUserId[E <: Event](
    event: E,
    filter: Filter)(
    implicit A: UserIdGetter[E]) = A.userId(event).contains(filter.userId)

  println {
    // implicit not found!
    // allEvents.filter(e => filterByUserId(e, Filter(UserId("1")))) (2)
  }

  // делаем так, чтобы (2) начал компилироваться
  // Coproduct in action

  implicit def eventCNil: UserIdGetter[CNil] = new UserIdGetter[CNil] {
    override def userId(t: CNil): Option[UserId] = t.impossible
  }

  implicit def eventCCons[H, T <: Coproduct](
    implicit sh: UserIdGetter[H],
    st: UserIdGetter[T]
  ): UserIdGetter[H :+: T] = new UserIdGetter[H :+: T] {

    override def userId(t: H :+: T): Option[UserId] = t match {
      case Inl(l) => sh.userId(l)
      case Inr(r) => st.userId(r)
    }
  }

  println {
//    allEvents.filter(e => filterByUserId(e, Filter(UserId("1"))))
  }

}
