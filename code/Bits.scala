package bits
import scala.*

// Compute the current memory usage of the JVM.
// Note that the JVM does many things behind the curtains, and in particular
// consumes some amount of memory: the measure will thus typically be quite
// noisy.
def memoryUsage: Long =
  val bytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
  bytes

val bools = 800_000_000;

// Expriment: how much storage is dedicated to a boolean in an array?
@main
def bits = {
  Runtime.getRuntime().gc();
  Thread.sleep(1000);
  val start = memoryUsage
  val arr = Array.fill(bools)(true)
  val end = memoryUsage
  println(arr(0).toString ++ " " ++ arr(arr.length-1).toString())

  // Divide by 8 to convert bits -> bytes
  println("Expected: " ++ (bools / 8).toString());
  println("Measured: " ++ (end - start).toString());
  // Measured is rougly 8x Expected:
  // Conlusion: a single boolean takes one whole byte of space.
}


// Repeat the exact same expriment, but using a `PackedArray` instead.
import PackedArray.*
@main
def packedBits = {
  Runtime.getRuntime().gc();
  Thread.sleep(1000);
  val start = memoryUsage
  val arr = PackedArray.fill(bools)(true)
  val end = memoryUsage
  println(arr(0).toString ++ " " ++ arr(arr.length-1).toString())

  println("Expected: " ++ (bools / 8).toString());
  println("Measured: " ++ (end - start).toString());
  // This time, the measurement matches (up to noise) the expected value.
}


