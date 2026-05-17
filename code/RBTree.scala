/*
  1. Every node is either Red or Black
  2. The root is Black
  3. Every leaf is black
  4. If a node is Red, then both its children are Black
  5. For each node, all simple paths from the node to descendants leaves
     contain the same number of Black nodes.
*/

private object Impl {
  enum Color {
    case Red()
    case Black()
  }
  import Color.*

  // This GADT is used to enforce the 4th invariant. This is done by "casing"
  // on the color of the tree. If the tree is `Red`, constructor `IRed` can
  // be used as long as the subtrees are colored `Black`. Otherwise, if
  // the tree is black, constructor `IBlack` can be used, regardless of the
  // color of the subtrees.
  enum Invariant4[C, Cl, Cr] {
    case IRed() extends Invariant4[Red, Black, Black]
    case IBlack() extends Invariant4[Black, Cl, Cr]
  }
  import Invariant4.*

  /*
    The invariants are enforced as follows:
      1. The type parameter `C` represents the color of the tree.

         Note: by itself, the type parameter does not force the tree to be
         red or black: the tree could be "colored with any type", as non sensical
         as that sounds. The presence of an `Invariant4` member actually forces
         the color to be red or black.

      2. We do not expose `RBTree` to users: instead, they are presented with
         the `BTree` type, which requires the root to be black.

      3. `RBTree` is a GADT, so we can "simply" force the color of leafs to be
          Black.

      4. `Fork` carry an "evidence" (of type `Invariant4`), which, by its existence,
          ensures that the node's color (`C`) is compatible with the colors
          of its children (`Cl` and `Cr`).

  */
  enum RBTree[T, C] {
    case
      Leaf()
      extends RBTree[T, Black]
    case
      Fork[T, C, Cl, Cr](ci: Invariant4[C, Cl, Cr], l: RBTree[T, Cl], v: T, r: RBTree[T, Cr])
      extends RBTree[T, C]
  }
  import RBTree.*
}

// A `Black Tree` is a "black" Red-Black Tree.
enum BTree[T] {
  case Root(t: Impl.RBTree[T, Impl.Color.Black])
}

@main
def testRBT =
  /*
          *
         /
        1
       / \
      /   *
     /
    2       *
     \     /
      \   3
       \ / \
        4   *
         \
          *
  */
  import Impl.Color.*
  import Impl.RBTree.*
  import Impl.Invariant4.*
  import BTree.*
  val t = Root(
    Fork[Int, Black, Black, Black](IBlack(),
        Fork[Int, Black, Black, Black](IBlack(),
            Leaf(),
          1,
            Leaf()
        ),
      2,
        // Attempting to color this node Red would fail:
        // one of its children is already red, breaking invariant 4.
        Fork[Int, Black, Red, Black](IBlack(),
            Fork[Int, Red, Black, Black](IRed(),
                Leaf(),
              3,
                Leaf()
            ),
          4,
          Leaf()
        )
    ))
  println(t)
