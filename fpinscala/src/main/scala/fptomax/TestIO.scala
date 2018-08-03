package fptomax

case class TestData(input: List[String], output: List[String], nums: List[Int]) {
  def putStrLine(line: String): (TestData, Unit) =
    (copy(output = line :: output), ())

  def getStrLine: (TestData, String) =
    (copy(input = input.drop(1)), input.head)

  def nextInt(upper: Int): (TestData, Int) =
    (copy(nums = nums.drop(1)), nums.head)

  def showResults = output.reverse.mkString("\n")

}

// State Monad?
case class TestIO[A](run: TestData => (TestData, A)) {
  self =>
  def map[B](ab: A => B): TestIO[B] =
    TestIO(t => self.run(t) match {
      case (t, a) => (t, ab(a))
    })
  def flatMap[B](afb: A => TestIO[B]): TestIO[B] =
    TestIO(t => self.run(t) match {
      case (t, a) => afb(a).run(t)
    })

  def eval(t: TestData): TestData = run(t)._1

}

object TestIO {
  def point[A](a: => A): TestIO[A] = TestIO(t => (t, a))

  implicit val ProgramTestIO: Program[TestIO] = new Program[TestIO] {
    override def finish[A](a: => A): TestIO[A] = TestIO.point(a)
    override def chain[A, B](fa: TestIO[A], afb: A => TestIO[B]): TestIO[B] = fa.flatMap(afb)
    override def map[A, B](fa: TestIO[A], ab: A => B): TestIO[B] = fa.map(ab)
  }

  implicit val ConsoleTestIO: Console[TestIO] = new Console[TestIO] {
    override def putStrLine(line: String): TestIO[Unit] = TestIO(t => t.putStrLine(line))
    override def getStrLine: TestIO[String] = TestIO(t => t.getStrLine)
  }

  implicit val RandomTestIO: Random[TestIO] = (upper: Int) => TestIO(t => t.nextInt(upper))

}