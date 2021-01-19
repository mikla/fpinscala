package model

/**
  * Smart constructors
  */
final case class Email private (value: String)

object Email {
  def fromString(value: String): Option[Email] =
    if (isValid(value)) Some(new Email(value)) else None

  def isValid(str: String) = true

  private def apply(value: String): Email = new Email(value)
}
