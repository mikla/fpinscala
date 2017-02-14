import io.print

val f: Int => Int = 1 + _
val f1: Int => Int = 1 +

case class A(value: Int) {
  def :::(that: A): A = {
    print(this.value) // THIS!
    that // THAT!
  }

  def +(that: A): A = A(this.value + that.value)

  def *(that: A): A = A(this.value * that.value)
}

A(0) ::: (A(1) ::: A(2) ::: A(3))

case class ++[A, B](a: A, b: B)

A(5) + A(2) * A(3)





