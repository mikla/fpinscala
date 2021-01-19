package stuff.hackerrank

object QueensAttack2App extends App {

  import stuff.hackerrank.QueensAttack2App.ObstacleDirection._

  trait ObstacleDirection extends Product with Serializable {
    def distance(n: Int, r_q: Int, c_q: Int, obstacle: Option[Coord]): Int
  }

  object ObstacleDirection {

    val All = Seq(LeftUp, LeftDown, RightUp, RightDown, CenterLeft, CenterUp, CenterDown, CenterRight)

    case object LeftUp extends ObstacleDirection {
      override def distance(n: Int, r_q: Int, c_q: Int, obstacle: Option[(Int, Int)]): Int =
        if (r_q == 1 || c_q == 1) 0
        else
          obstacle.map { case (row, col) =>
            if (c_q >= r_q) r_q - row - 1 else c_q - col - 1
          }.getOrElse {
            if (c_q >= r_q) r_q - 1 else c_q - 1
          }
    }

    case object LeftDown extends ObstacleDirection {
      override def distance(n: Int, r_q: Int, c_q: Int, obstacle: Option[(Int, Int)]): Int =
        if (r_q == n || c_q == 1) 0
        else {
          obstacle.map { case (row, col) =>
            if (c_q < n - r_q) c_q - col - 1 else row - r_q - 1
          }.getOrElse {
            if (c_q < n - r_q) c_q - 1
            else n - r_q
          }
        }
    }

    case object RightUp extends ObstacleDirection {
      override def distance(n: Int, r_q: Int, c_q: Int, obstacle: Option[(Int, Int)]): Int =
        if (c_q == n || r_q == 1) 0
        else {
          obstacle.map { case (row, col) =>
            if (r_q + 1 >= c_q) r_q - row - 1 else col - c_q - 1
          }.getOrElse {
            if (r_q + 1 >= c_q) r_q - 1 else n - c_q
          }
        }
    }

    case object RightDown extends ObstacleDirection {
      override def distance(n: Int, r_q: Int, c_q: Int, obstacle: Option[(Int, Int)]): Int =
        if (r_q == n || c_q == n) 0
        else
          obstacle.map { case (row, col) =>
            if (c_q <= r_q) row - r_q - 1 else col - c_q - 1
          }.getOrElse {
            if (c_q <= r_q) n - r_q else n - c_q
          }
    }

    case object CenterLeft extends ObstacleDirection {
      override def distance(n: Int, r_q: Int, c_q: Int, obstacle: Option[(Int, Int)]): Int =
        if (c_q == 1) 0
        else
          obstacle.map { case (row, col) =>
            c_q - col - 1
          }.getOrElse(c_q - 1)
    }

    case object CenterUp extends ObstacleDirection {
      override def distance(n: Int, r_q: Int, c_q: Int, obstacle: Option[(Int, Int)]): Int =
        if (r_q == 1) 0
        else
          obstacle.map { case (row, _) =>
            r_q - row - 1
          }.getOrElse(r_q - 1)
    }

    case object CenterDown extends ObstacleDirection {
      override def distance(n: Int, r_q: Int, c_q: Int, obstacle: Option[(Int, Int)]): Int =
        if (c_q == n) 0
        else
          obstacle.map { case (row, _) =>
            row - r_q - 1
          }.getOrElse(n - r_q)
    }

    case object CenterRight extends ObstacleDirection {
      override def distance(n: Int, r_q: Int, c_q: Int, obstacle: Option[(Int, Int)]): Int =
        if (r_q == n) 0
        else
          obstacle.map { case (row, col) =>
            col - c_q - 1
          }.getOrElse(n - c_q)
    }

  }

  type Coord = (Int, Int)
  type ObstacleMap = Map[ObstacleDirection, Option[Coord]]

  def queensAttack(n: Int, k: Int, r_q: Int, c_q: Int, obstacles: Array[Array[Int]]): Int = {
    val mainInvariant = r_q + c_q
    val secondInvariant = r_q - c_q

    val initial = ObstacleDirection.All.map(_ -> Option.empty[Coord]).toMap

    val nearest = obstacles.foldLeft(initial) { case (acc, coord) =>
      val (row, col) = (coord(0), coord(1))

      if (row + col == mainInvariant) { // RightUp | LeftDown
        if (col > c_q) { // RightUp
          val v = acc.get(RightUp).flatten
          if (v.forall(_._1 < row)) acc.updated(RightUp, Some((row, col))) else acc
        } else { // LeftDown
          val v = acc.get(LeftDown).flatten
          if (v.forall(_._2 < col)) acc.updated(LeftDown, Some((row, col))) else acc
        }
      } else if (row - col == secondInvariant) { // RightDown | LeftUp
        if (col > c_q) { // RightDown
          val v = acc.get(RightDown).flatten
          if (v.forall(_._1 > row)) acc.updated(RightDown, Some((row, col))) else acc
        } else { // LeftUp
          val v = acc.get(LeftUp).flatten
          if (v.forall(_._2 < col)) acc.updated(LeftUp, Some((row, col))) else acc
        }
      } else if (col == c_q) { // CenterUp | CenterDown
        if (row < r_q) {
          val v = acc.get(CenterUp).flatten
          if (v.forall(_._1 < row)) acc.updated(CenterUp, Some((row, col))) else acc
        } else {
          val v = acc.get(CenterDown).flatten
          if (v.forall(_._1 > row)) acc.updated(CenterDown, Some((row, col))) else acc
        }
      } else if (row == r_q) {
        if (col < c_q) { // CenterLeft
          val v = acc.get(CenterLeft).flatten
          if (v.forall(_._2 < col)) acc.updated(CenterLeft, Some((row, col))) else acc
        } else { // CenterRight
          val v = acc.get(CenterRight).flatten
          if (v.forall(_._2 > col)) acc.updated(CenterRight, Some((row, col))) else acc
        }
      } else acc
    }

    nearest.map { case (key, obstable) =>
      val d = key.distance(n, r_q, c_q, obstable)
      println(key + ": " + d)
      d
    }.sum
  }

  val obstacles1 = Array(
    Array(2, 7),
    Array(8, 9),
    Array(9, 10),
    Array(6, 3),
    Array(1, 2),
    Array(2, 5),
    Array(8, 5),
    Array(4, 2),
    Array(4, 9),
    Array(2, 3)
  )

  //  val ans = queensAttack(10, 9, 4, 5, obstacles1)

  //  println(queensAttack(4, 0, 4, 4, Array.empty))

  /*
  println(queensAttack(5, 3, 4, 3,
    Array(
      Array(5, 5),
      Array(4, 2),
      Array(2, 3)
    )
  ))
   */

  println(RightUp.distance(5, 4, 4, None))
}
