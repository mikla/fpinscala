package datastructure.tree

sealed trait Tree[+A]

case class Leaf[A](value: A) extends Tree[A]

case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {

  def size[A](tree: Tree[A]): Int = {
    tree match {
      case Leaf(_) => 1
      case Branch(left, right) => size(left) + size(right) + 1
    }
  }

  def foldLeft[A, B](tree: Tree[A], z: B)(f: (B, A) => B) = {
    def loop(left: Tree[A], queue: Tree[A], acc: B): B = {
      left match {
        case Leaf(v) =>
          queue match {
            case Leaf(b) => f(f(acc, b),v)
            case Branch(ll, rr) => loop(ll, rr, f(acc, v))
          }
        case Branch(l, r) => loop(l, Branch(r, queue), acc)
      }
    }

    tree match {
      case Leaf(v) => f(z, v)
      case Branch(l, r) => loop(l, r, z)
    }

  }

  def sizeTail[A](tree: Tree[A]): Int = {
    Tree.foldLeft(tree, 0)((x, y) => x + 1)
  }

}