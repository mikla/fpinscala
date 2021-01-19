package fp_in_scala.datastructure.tree

sealed trait Tree[+A]

case class Leaf[A](value: A) extends Tree[A]

case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {

  def foldLeft[A, B](tree: Tree[A], z: B)(f: (B, A) => B) = {
    def loop(left: Tree[A], queue: List[Tree[A]], acc: B): B =
      left match {
        case Leaf(v) =>
          val accVal = f(acc, v)
          queue match {
            case Nil => accVal
            case x :: xs => loop(x, xs, accVal)
          }
        case Branch(l, r) => loop(l, r :: queue, acc)
      }
    loop(tree, Nil, z)
  }

  def size[A](tree: Tree[A]): Int = Tree.foldLeft(tree, 0)((x, y) => x + 1)

  def reduce[B >: A, A](tree: Tree[A])(f: (B, B) => B): B = {
    def loop(left: Tree[A], queue: List[Tree[A]], acc: B => B): B =
      left match {
        case Leaf(v) =>
          queue match {
            case Nil => acc(v)
            case x :: xs => loop(x, xs, f(acc(v), _))
          }
        case Branch(l, r) => loop(l, r :: queue, acc)
      }
    loop(tree, Nil, identity)
  }

  def maximum[T : Ordering](tree: Tree[T]): T = Tree.reduce(tree)((x, y) => implicitly[Ordering[T]].max(x, y))

  def depth[A](tree: Tree[A]): Int = {
    def loop(left: Tree[A], queue: List[(Int, Tree[A])], loopDepth: Int, maxAcc: Int): Int =
      left match {
        case Leaf(v) =>
          queue match {
            case Nil => maxAcc
            case x :: xs =>
              val (leafAcc, leafLeft) = x
              loop(leafLeft, xs, leafAcc, Ordering[Int].max(maxAcc, leafAcc))
          }
        case Branch(l, r) => loop(l, (loopDepth + 1, r) :: queue, loopDepth + 1, maxAcc)
      }
    loop(tree, Nil, 0, 0)
  }

  def toString[A](tree: Tree[A]): String =
    tree match {
      case Leaf(v) => s"Leaf($v)"
      case Branch(left, right) => s"Branch(${toString(left)},${toString(right)})"
    }

  def map[A, B](tree: Tree[A])(f: A => B): Tree[B] =
    tree match {
      case Leaf(v) => Leaf(f(v))
      case Branch(left, right) => Branch(map(left)(f), map(right)(f))
    }

  def fold[A, B](t: Tree[A])(f: A => B)(g: (B, B) => B): B = t match {
    case Leaf(a) => f(a)
    case Branch(l, r) => g(fold(l)(f)(g), fold(r)(f)(g))
  }

  def sizeViaFold[A](t: Tree[A]): Int =
    fold(t)(a => 1)(_ + _)

  def depthViaFold[A](t: Tree[A]): Int =
    fold(t)(a => 0)((d1, d2) => 1 + (d1.max(d2)))

  def mapViaFold[A, B](t: Tree[A])(f: A => B): Tree[B] =
    fold(t)(a => Leaf(f(a)): Tree[B])(Branch(_, _))
}
