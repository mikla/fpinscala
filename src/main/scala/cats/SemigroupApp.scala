package cats

import cats.implicits._

object SemigroupApp extends App {

  Semigroup[Int].combine(1, 2) // 3

  Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6)) // List(1, 2, 3, 4, 5, 6)

  Semigroup[Option[Int]].combine(Option(1), Option(2)) // Option(3)

  Semigroup[Option[Int]].combine(Option(1), None) // Some(1)

  // apply 6 to each function and then combine them like Ints
  Semigroup[Int ⇒ Int].combine({ (x: Int) ⇒ x + 1 }, { (x: Int) ⇒ x * 10 }).apply(6) // 67

  println {
    Map("foo" → Map("bar" → 5))
      .combine(Map("foo" → Map("bar" → 6), "bar" → Map()))
  }

  println("Combining maps with just Scala")
  val m1 = Map("foo" → Map("bar" → 5))
  val m2 = Map(
    "foo" → Map("bar" → 6),
    "bar" → Map.empty[String, Int]
  )

  println {
    (m1.keySet ++ m2.keySet).map { k ⇒
      (k, (m1.getOrElse(k, Map.empty).keySet ++ m2.getOrElse(k, Map.empty).keySet).map { k2 ⇒
        (k2, m1.getOrElse(k, Map.empty).getOrElse(k2, 0) + m2.getOrElse(k, Map.empty).getOrElse(k2, 0))
      }.toMap)
    }.toMap
  }

  println {
    Map("foo" -> List(1, 2)).combine(Map("foo" -> List(3, 4), "bar" -> List(42)))
  }

  println {
    Map("foo" -> Map("bar" -> 5)) ++ Map("foo" -> Map("bar" -> 6), "baz" -> Map())
  }

  println {
    Map("foo" -> List(1, 2)) ++ Map("foo" -> List(3, 4), "bar" -> List(42))
  }

}
