package PackedArray

case class PackedArray private(size: Int, buffer: Array[Int]) {
  def length = size

  private def inBounds(at: Int) = assert(0 <= at && at < size)

  def apply(at: Int): Boolean = {
    inBounds(at)
    val elem = buffer(PackedArray.arrayIndex(at));
    val bit = (elem >> PackedArray.elemOffset(at)) & 0x1;
    bit == 0x1
  }

  def update(at: Int, v: Boolean) = {
    inBounds(at)
    val mask = 0x1 << PackedArray.elemOffset(at);
    if v then
      buffer(PackedArray.arrayIndex(at)) |= mask;
    else
      buffer(PackedArray.arrayIndex(at)) &= ~mask;
  }
}

object PackedArray {
  private def arrayIndex(at: Int) = at / 32;
  private def elemOffset(at: Int) = at % 32;

  def fill(n: Int)(v: Boolean) = {
    val size: Int = arrayIndex(n) + (if elemOffset(n) != 0 then 1 else 0);
    val init: Int = if v then 0xffffffff else 0x0;
    PackedArray(n, Array.fill(size)(init))
  }
}

import scala.util.Random
@main
private def testPackedArray = {
  val rng = Random();

  def test (size: Int) = {
    val arr = PackedArray.fill(size)(true);

    for (i <- 0 until size) {
      assert(arr(i) == true)
      val b = rng.nextBoolean();
      arr(i) = b;
      assert(arr(i) == b);
    }

    for (i <- 0 until size) {
      val b = arr(i);
      arr(i) = !b;
      assert(!arr(i) == b);
    }
  }

  for(i <- 0 to 1000) {
    test(i)
  }

  for (i <- 0 to 100) {
    val base = 10000;
    val size = rng.nextInt(base) + base;
    test(size)
  }
  println("All done!")
}
