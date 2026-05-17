package Tree

object OOP {

  trait Tree[T] {
    def isLeaf: Boolean
    // Can only be used safely if `isLeaf` returned `false`.
    def get: T
    // Can only be used safely if `isLeaf` returned `false`.
    def left: Tree[T]
    // Ditto.
    def right: Tree[T]
  }
  case class Leaf[T](v: T) extends Tree[T] {
    def isLeaf: Boolean = true
    def get: T = v
    // `???` is a quick and dirty way of throwing an exception.
    def left: Tree[T] = ???
    def right: Tree[T] = ???
  }
  case class Fork[T](l: Tree[T], r: Tree[T]) extends Tree[T] {
    def isLeaf: Boolean = false
    def get: T = ???
    def left: Tree[T] = l
    def right: Tree[T] = r
  }

  def map[S, T](t: Tree[S], f: S => T): Tree[T] =
    if t.isLeaf
    then
      Leaf(f(t.get))
    else
      val l1 = map(t.left, f)
      val r1 = map(t.right, f)
      Fork(l1, r1)

  @main
  def testOOP =
    val t = Fork(Fork(Leaf(21), Leaf(23)), Leaf(38))
    println(map(t, x => 2*x))
}


object OOP_With_Pattern_Matching {

  trait Tree[T]
  case class Leaf[T](v: T) extends Tree[T]
  case class Fork[T](l: Tree[T], r: Tree[T]) extends Tree[T]
  // Commenting this out will not result in any warning...
  /* case class Trunk[T](t: Tree[T]) extends Tree[T] */

  def map[S, T](t: Tree[S], f: S => T): Tree[T] =
    // ... despite this match not being exhaustive...
    t match
      case t: Leaf[_] =>
        Leaf(f(t.v))
      case t: Fork[_] =>
        val l1 = map(t.l, f)
        val r1 = map(t.r, f)
        Fork(l1, r1)


  @main
  def testOOPpm =
    val t = Fork(Fork(Leaf(21), Leaf(23)), Leaf(38))
    // ... meaning that using this tree would result in a runtime error.
    /* val t = Fork(Fork(Trunk(Leaf(21)), Leaf(23)), Leaf(38)) */
    println(map(t, x => 2*x))
}

object ADT {
  // Despite using the `enum` keyword, this does not declare an enumeration:
  // this declares an Algebraic Datatype (ADT).
  //
  // Note: the distinction between an enumeration and an ADT whose
  // constructors do not take arguments it tenuous, if not meaningless.
  enum Tree[T] {
    case Leaf(v: T) extends Tree[T]
    case Fork(l: Tree[T], r: Tree[T]) extends Tree[T]
    case Trunk(t: Tree[T]) extends Tree[T]
  }
  // Need to import the constructors, or refer to them as, e.g., `Tree.Leaf`.
  import Tree.*

  def map[S, T](t: Tree[S], f: S => T): Tree[T] =
    t match
      // We can re-use the exact same code as with OOP...
      case t: Leaf[_] =>
        Leaf(f(t.v))
      // .. or improve on it by binding members directly.
      case Fork(l, r) =>
        val l1 = map(l, f)
        val r1 = map(r, f)
        Fork(l1, r1)
      // (Compare with:)
      /*
      case t: Fork[_] =>
        val l1 = map(t.l, f)
        val r1 = map(t.r, f)
        Fork(l1, r1)
      */
      case Trunk(t) => Trunk(map(t, f))


  @main
  def testADT =
    val t = Fork(Fork(Trunk(Leaf(21)), Leaf(23)), Leaf(38))
    println(map(t, x => 2*x))

}