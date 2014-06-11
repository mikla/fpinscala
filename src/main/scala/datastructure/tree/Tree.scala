package datastructure.tree

sealed trait Tree[+A]

case class Leaf[A](value: A) extends Tree[A]

case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {

  def foldLeft[A, B](tree: Tree[A], z: B)(f: (B, A) => B) = {
    def loop(left: Tree[A], queue: List[Tree[A]], acc: B): B = {
      left match {
        case Leaf(v) =>
          val accVal = f(acc, v)
          queue match {
            case Nil => accVal
            case x :: xs => loop(x, xs, accVal)
          }
        case Branch(l, r) => loop(l, r :: queue, acc)
      }
    }
    loop(tree, Nil, z)
  }

  def size[A](tree: Tree[A]): Int = Tree.foldLeft(tree, 0)((x, y) => x + 1)

  def maximum(tree: Tree[Int]): Int = Tree.foldLeft(tree, Int.MinValue)(_ max _)

}