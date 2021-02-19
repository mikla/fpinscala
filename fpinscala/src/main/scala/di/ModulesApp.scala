package di

import monix.eval.Task

object ModulesApp extends App {

  trait JobLogService

  trait CommonModule {

    def init(transactor: String, config: String): CommonModule

    def config: String
    def jobLogService: JobLogService
  }

  class EsboMain extends CommonModule {
    override def jobLogService: JobLogService = new JobLogService {}
    override def init(transactor: String, config: String): EsboMain = ???
    override def config: String = ???
  }

  class PavsModule extends CommonModule {
    override def jobLogService: JobLogService = new JobLogService {}
    override def init(transactor: String, config: String): PavsModule = ???
    override def config: String = ???
  }

  trait BaseMain[T <: CommonModule] {

    val m: T

    def run: Task[Int] = {

      val transactor = ""
      val config = ""

      val modules = m.init(transactor, config)

      modules.jobLogService

      Task.now(0)
    }

  }

}
