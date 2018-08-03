package fptomax

trait Console[F[_]] {
  def putStrLine(line: String): F[Unit]
  def getStrLine: F[String]
}

object Console {
  def apply[F[_]](implicit F: Console[F]): Console[F] = F
  def putStrLine[F[_]: Console](line: String): F[Unit] = Console[F].putStrLine(line)
  def getStrLine[F[_]: Console]: F[String] = Console[F].getStrLine
}
