import fsis.applicative.Applicative
import Applicative.{optionApplicative, listApplicative}

optionApplicative.map(Some(1))(_ + 1)

optionApplicative.map(None: Option[Int])(_ + 1)

listApplicative.map(List(1, 2, 3))(_ + 1)

optionApplicative.map2(Some(1), Some(2))(_ + _)

listApplicative.map2(List(1, 2, 3), List(4, 5, 6))(_ + _)

optionApplicative.map3(Some(1), Some(2), Some(4))(_ + _ + _)

optionApplicative.tuple2(Some(2), Some(3))

listApplicative.tuple2(List(1, 2, 3), List(4, 5, 6))

optionApplicative.map4(Some(1), Some(2), Some(4), Some(5))(_ + _ + _ + _)