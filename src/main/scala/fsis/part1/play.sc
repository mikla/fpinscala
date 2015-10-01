def foo[A](x: A, y: A) = ???
def bar[F[_], A](x: F[A], y: F[A]) = 1
bar(1, List(1))

