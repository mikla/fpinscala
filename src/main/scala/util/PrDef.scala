package util


object PrDef {

  def flip[A, B, C](f: (A, B) => C): (B, A) => C = (b: B, a: A) => f(a, b)

}
