package es

import cats.syntax.either._

/**
  * Defines aggregate reaction to commands and events
  *
  * @tparam A Type of the pure aggregate root, for which behaviour is defined
  * @tparam C Commands
  * @tparam E Events
  * @tparam R Rejections (command validation errors)
  */
trait AggregateRootBehaviour[A, C, E, R] {

  type CommandReaction = Either[R, List[E]]
  object CommandReaction {
    def accept(events: E*): CommandReaction = Right(events.toList)
    def reject(error: R): CommandReaction = Left(error)
  }

  def processCommand(state: EventualState[A])(command: C): CommandReaction = state match {
    case NotInitialized => processInitializationCommand(command)
    case Initialized(st) => processLifecycleCommand(st)(command)
  }
  def applyEvent(state: EventualState[A])(event: E): Initialized[A] = state match {
    case NotInitialized => Initialized(applyInitializationEvent(event))
    case Initialized(st) => Initialized(applyLifecycleEvent(st)(event))
  }

  // todo: Has purely testing purposes, a subject to be moved to some other place
  /**
    * A helper method for processing commands in batch without persisting events.
    */
  def processCommandsAndEvents(aggregateState: EventualState[A])(commands: List[C]): Either[R, A] =
    commands.foldLeft(Either.right[R, EventualState[A]](aggregateState)) {
      case (rejectOrState, command) => rejectOrState match {
        case Left(_) => rejectOrState
        case Right(state) =>
          processCommand(state)(command).map(
            events => events.foldLeft(state) {
              case (st, e) => applyEvent(st)(e)
            }
          )
      }
    }.map(_.asInstanceOf[Initialized[A]].state)

  /**
    * Processes a sequence of commands, incrementally evolving initial state after each command.
    *
    * @return A list of events produced by all commands in synchronized order, or a rejection.
    */
  def processLifecycleCommandSequence(state: A)(commands: C*): CommandReaction =
    commands.foldLeft(Either.right[R, (List[E], A)](List.empty[E] -> state)) {
      case (rejectOrStateAndEvents, command) => rejectOrStateAndEvents match {
        case Left(_) => rejectOrStateAndEvents
        case Right((events, accState)) =>
          processLifecycleCommand(accState)(command).map(
            newEvents => (events ++ newEvents, newEvents.foldLeft(accState) {
              case (st, e) => applyLifecycleEvent(st)(e)
            })
          )
      }
    }.map(_._1)

  def applyLifecycleEventSequence(state: A)(events: E*): A =
    events.foldLeft(state) { case (s, e) => applyLifecycleEvent(s)(e) }

  def invalidEventReceived(e: E): A = throw new Exception(s"Received invalid event: $e")

  def processInitializationCommand(command: C): CommandReaction
  def processLifecycleCommand(state: A)(command: C): CommandReaction

  def applyInitializationEvent(event: E): A
  def applyLifecycleEvent(state: A)(event: E): A

  // constraints
  type Constraint = A => Option[R]
  val NoConstraint: Constraint = _ => None

  def validate(state: A, constraints: Constraint*)(ifNoErrors: => CommandReaction): CommandReaction =
    constraints.foldLeft(None: Option[R]) {
      case (rej, constr) => if (rej.isDefined) rej else constr(state)
    }
      .map(CommandReaction.reject)
      .getOrElse(ifNoErrors)

  implicit class BooleanConstraintOps(b: Boolean) {
    def ifTrue(reject: => R): Option[R] = if (b) Some(reject) else None
    def ifFalse(reject: => R): Option[R] = if (!b) Some(reject) else None
  }

  implicit class OptionConstraintOps[T](o: Option[T]) {
    def ifDefined(rejector: T => R): Option[R] = o.map(rejector)
    def ifNone(reject: => R): Option[R] = if (o.isEmpty) Some(reject) else None
  }

  implicit class ListConstraintOps[T](o: List[T]) {
    def ifNotEmpty(rejector: List[T] => R): Option[R] = o.headOption.map(_ => rejector(o))
  }

  implicit class ConstraintOps[T](c: Constraint) {
    def invert(withError: => R): Constraint = c.andThen {
      case Some(_) => None
      case None => Some(withError)
    }
    def and(another: Constraint): Constraint =
      state => c(state).orElse(another(state))
  }

  object Constraint {
    def sequence(constraints: List[Constraint]): Constraint =
      loc => constraints.foldLeft(None: Option[R]) {
        case (res, constraint) => res.orElse(constraint(loc))
      }
  }
}
