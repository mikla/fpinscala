package fixpoint

object FixPointApp extends App {

  // recursive data type
  // Recursion is hard.
  // Recursion and Corecursion

  // Simple recursive data type

  case class PProf(
    name: String,
    year: Int,
    students: List[PProf])

  // Problem?
  // How we store this to DB?
  // Basically, we will have some parent id.
  // But id is generated by DB.

  case class ProfF[A](
    name: String,
    year: Int,
    students: List[A])

  //  type ProfT = ProfF[Prof]

  //  Illegal cyclic reference
  //  type IdProf = (Int, ProfF[IdProf])

  // but classes can be recursive

  //  case class Prof[F[_]](value: F[Prof[F]]) // <- This is called Fix[F[_]]
  //  case class IdProf[F[_], A](id: A, value: F[IdProf[F, A]]) // <- Cofree[F[_], A]

  case class Fix[F[_]](unfix: F[Fix[F]])

  case class Cofree[F[_], A](head: A, tail: F[Cofree[F, A]])

  case class Free[F[_], A](resume: Either[A, F[Free[F, A]]])

  // Define .flatMap on Free and write little DSL
  // It will not be stack-safe.

  case class CofreeF[F[_], A, B](head: A, tail: F[B])

  case class FreeF[F[_], A, B](resume: Either[A, F[B]])

  type CofreeT[F[_], A] = Fix[CofreeF[F, A, *]]
  type FreeT[F[_], A] = Fix[FreeF[F, A, *]]

  // back to Prof / IdProf examples

  type Prof = Fix[ProfF]
  type IdProf = Cofree[ProfF, Int]

  // How we can construct?

  val p: PProf =
    PProf(
      "Hilber",
      1887,
      List(
        PProf("Ackeman", 1025, Nil),
        PProf("Curry", 1930, Nil),
        PProf(
          "Weyl",
          1908,
          List(
            PProf(
              "Mac Lane",
              1934,
              List(
                PProf("Howard", 1956, Nil),
                PProf("Awodey", 1997, Nil)
              ))
          ))
      )
    )

  val p1: IdProf =
    Cofree(
      1,
      ProfF(
        "Hilber",
        1887,
        List(
          Cofree(2, ProfF("Ackeman", 1025, Nil)),
          Cofree(3, ProfF("Curry", 1930, Nil)),
          Cofree(
            4,
            ProfF(
              "Weyl",
              1908,
              List(
                Cofree(
                  5,
                  ProfF(
                    "Mac Lane",
                    1934,
                    List(
                      Cofree(6, ProfF("Howard", 1956, Nil)),
                      Cofree(7, ProfF("Awodey", 1997, Nil))
                    ))))))
        )
      )
    )

  // Lets parse something to Fix

  // unfoldCM - Comonadic corecursion!

  // Bacically,
  // We can annotate tree values with some metadata using Cofree.

}
