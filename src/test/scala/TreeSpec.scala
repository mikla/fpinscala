import datastructure.tree.{Leaf, Branch, Tree}
import org.scalatest.FlatSpec

class TreeSpec extends FlatSpec {

  "List folding sum" should "return sum of alements" in {
    val tree = Branch(Branch(Branch(Leaf(1), Leaf(2)), Leaf(3)), Leaf(4))
    assert(Tree.size(tree) == 7)
  }

}
