package application

import cats.Monoid
import cats.implicits.catsSyntaxSemigroup
import dtc.TimePoint
import shapelessex.application.UserId

object Scala2135ContextBoundsIssue extends App {

  sealed abstract class Defect[T : TimePoint] {
    locally {
      val _ = implicitly[TimePoint[T]]
    }

    def fn[TT : TimePoint](f: T => TT): Defect[TT]
  }

  case class Defect1[T : TimePoint](id: String, t: T) extends Defect[T] {
    override def fn[TT : TimePoint](f: T => TT): Defect[TT] = ???
  }

  sealed abstract class DefectEx[T] {
    def fn[TT : TimePoint](f: T => TT)(implicit TP: TimePoint[T]): DefectEx[TT]
  }

  case class DefectEx1[T](id: String, t: T) extends DefectEx[T] {
    override def fn[TT : TimePoint](f: T => TT)(implicit TP: TimePoint[T]): DefectEx[TT] = ???
  }

  case class Defects[T](a: Defect[T])

  case class CommentContent()
  case class CommentId(id: Int) extends AnyVal

  case class Comment[T](
    id: CommentId,
    submittedBy: UserId,
    submittedAt: T,
    content: CommentContent) {

    def map[TT](f: T => TT): Comment[TT] = copy(submittedAt = f(submittedAt))
  }

  /**
    * Typesafe wrapper for all comments of single request
    */
  case class Comments[T](comments: List[Comment[T]]) {
    def add(comment: Comment[T]): Comments[T] = copy(comment :: comments)

    def allParticipants: List[UserId] = comments.map(_.submittedBy).distinct

    def map[TT](f: T => TT): Comments[TT] = copy(comments = comments.map(_.map(f)))
  }

  object Comments {
    def empty[T]: Comments[T] = Comments[T](Nil)

    implicit def monoid[T]: Monoid[Comments[T]] = new Monoid[Comments[T]] {
      def empty: Comments[T] = Comments.empty[T]
      def combine(x: Comments[T], y: Comments[T]): Comments[T] = Comments(x.comments |+| y.comments)
    }
  }

}
