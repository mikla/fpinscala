package datastructure

import datastructure.tree.{Branch, Leaf, Tree}
import org.scalatest.FlatSpec

class TreeSpec extends FlatSpec {

  "Tree.size" should "return sum of alements" in {
    val tree = Branch(Branch(Branch(Leaf(1), Leaf(2)), Leaf(3)), Leaf(4))
    assert(Tree.size(tree) == 4)
  }

  "Tree folding sum" should "return sum of alements" in {
    val tree = Branch(Branch(Leaf(4), Leaf(7)), Leaf(-3))
    assert(Tree.foldLeft(tree, 0)(_ + _) == 8)
    assert(Tree.foldLeft(tree, 0)(_ - _) == -8)
  }

  "Tree.maximum element" should "return 7" in {
    val tree = Branch(Branch(Leaf(-4), Leaf(-7)), Leaf(-3))
    assert(Tree.maximum(tree) == -3)
  }

  "Tree.reduce element" should "return 7" in {
    val tree = Branch(Branch(Branch(Leaf(-4), Leaf(-7)), Leaf(-3)),Branch(Leaf(7), Leaf(7)))
    assert(Tree.reduce(tree)(_ + _) == 0)
  }

  "Tree.depth element" should "return 5" in {
    val tree = Branch(Branch(Branch(Leaf(-4), Branch(Leaf(4), Branch(Leaf(4), Leaf(4)))), Leaf(-3)), Branch(Leaf(7), Leaf(3)))
    assert(Tree.depth(tree) == 5)
  }

  "Tree.map" should "return 5" in {
    val tree = Branch(Branch(Branch(Leaf(-4), Branch(Leaf(4), Branch(Leaf(4), Leaf(4)))), Leaf(-3)), Branch(Leaf(7), Leaf(3)))
    val transformedTree = Tree.map(tree)(_ + 1)
    assert(Tree.foldLeft(transformedTree, 0)(_ - _) == -22)
  }

}
