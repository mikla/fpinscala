import java.util.concurrent.{Future, ExecutorService}

package object parallel {

  type Par[A] = ExecutorService => Future[A]

}
