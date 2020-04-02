package fixpoint

object FixFucntion extends App {

  type Rec[T] = T => T

  def recursion[T](f: (=> Rec[T]) => Rec[T]): Rec[T] = f(recursion(f))

  println(recursion[Int](rec => x => if (x == 0) 0 else x + rec(x - 1))(7))

}
