package fptomax

trait Random[F[_]] {
  def nextInt(upper: Int): F[Int]
}

object Random {
  def apply[F[_]](implicit F: Random[F]): Random[F] = F
  def nextInt[F[_]](upper: Int)(implicit F: Random[F]): F[Int] = Random[F].nextInt(upper)
}
