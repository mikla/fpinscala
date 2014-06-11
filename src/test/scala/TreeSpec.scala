import datastructure.tree.{Leaf, Branch, Tree}
import org.scalatest.FlatSpec

class TreeSpec extends FlatSpec {

  "Tree.size" should "return sum of alements" in {
    val tree = Branch(Branch(Branch(Leaf(1), Leaf(2)), Leaf(3)), Leaf(4))
    assert(Tree.size(tree) == 4)
  }

  "List folding sum" should "return sum of alements" in {
    val tree = Branch(Branch(Leaf(4), Leaf(7)), Leaf(-3))
    assert(Tree.foldLeft(tree, 0)(_ + _) == 8)
    assert(Tree.foldLeft(tree, 0)(_ - _) == -8)
  }

}
