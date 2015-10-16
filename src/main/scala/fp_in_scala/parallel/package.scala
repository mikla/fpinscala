import java.util.concurrent.ExecutorService

package object parallel {

  sealed trait Future[A] {
    def apply(k: A => Unit): Unit
  }

  type Par[A] = ExecutorService => Future[A]

}
