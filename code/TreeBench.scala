//> using dep "org.openjdk.jmh:jmh-core:1.37"
package TreeBench

import scala.util.Random
import org.openjdk.jmh.annotations.{Fork => _, *}
import java.util.concurrent.TimeUnit

val seed = 0;
val size = 1000000;

abstract class TreeBench {
  type Tree[T]
  def leaf[T](v: T): Tree[T]
  def fork[T](l: Tree[T], r: Tree[T]): Tree[T]
  def map[S,T](t: Tree[S], f: S => T): Tree[T]

  var t: Tree[Int] = leaf(0);

  def generate(rng: Random, size: Int): Tree[Int] =
    if size <= 1
    then leaf(rng.nextInt())
    else
      val m = rng.nextInt(size-1)
      val l = generate(rng, m+1)
      val r = generate(rng, (size-2) - m)
      fork(l, r)

  @Setup
  def setup = {
    val rng = Random(seed);
    t = generate(rng, size)
  }

  @TearDown
  def check = {

  }

  @Benchmark
  @Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
  @Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
  def bench = {
    map(t, x => x + 100)
  }
}

@State(Scope.Thread)
class BenchOOP extends TreeBench {
  import Tree.{OOP => I}

  type Tree[T] = I.Tree[T]
  def leaf[T](v: T) = I.Leaf(v)
  def fork[T](l: Tree[T], r: Tree[T]) = I.Fork(l, r)
  def map[S,T](t: Tree[S], f: S => T) = I.map(t, f)
}


@State(Scope.Thread)
class BenchTypeTest extends TreeBench {
  import Tree.{OOP_With_Pattern_Matching => I}

  type Tree[T] = I.Tree[T]
  def leaf[T](v: T) = I.Leaf(v)
  def fork[T](l: Tree[T], r: Tree[T]) = I.Fork(l, r)
  def map[S,T](t: Tree[S], f: S => T) = I.map(t, f)
}

@State(Scope.Thread)
class BenchADT extends TreeBench {
  import Tree.{ADT => I}

  type Tree[T] = I.Tree[T]
  def leaf[T](v: T) = I.Tree.Leaf(v)
  def fork[T](l: Tree[T], r: Tree[T]) = I.Tree.Fork(l, r)
  def map[S,T](t: Tree[S], f: S => T) = I.map(t, f)
}
