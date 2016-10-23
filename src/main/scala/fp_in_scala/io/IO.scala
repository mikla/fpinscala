package fp_in_scala.io


trait IO { self =>
  def run: Unit

  def ++(io: IO): IO = new IO {
    override def run: Unit = {
      self.run
      io.run
    }
  }
}

object IO {
  def empty: IO = new IO {
    override def run: Unit = ()
  }

}


