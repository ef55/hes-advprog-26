import PackedArray.*

// Can we encompass `Array` and `PackedArray` under a common API?
// Yes, using GADTs!
enum SmartArray[T] {
  case Default(a: Array[T]) extends SmartArray[T]
  // A `PackedArray` can only be used to store booleans: we need a GADT
  // to enforce this constraint.
  case Compact(a: PackedArray) extends SmartArray[Boolean]
}
import SmartArray.*

def get[T](a: SmartArray[T], i: Int): T =
  a match
    case Default(a) => a(i)
    case Compact(a) => a(i)

@main
def testSmartArray =
  // Compact can be used for arrays of booleans.
  val a: SmartArray[Boolean] = Compact(PackedArray.fill(10)(true))
  // And default for any type.
  val b: SmartArray[Int] = Default(Array.fill(20)(10))
  // We can also use default for booleans.
  val c: SmartArray[Boolean] = Default(Array.fill(30)(false))
  // But we cannot use compact arrays for anything else!
  /* val c: SmartArray[Int] = Compact(PackedArray.fill(20)(false)) */