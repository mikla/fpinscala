package free.softwaremill


case class Tickets()

sealed trait External[A]
case class InvokeTicketingService(count: Int) extends External[Tickets]

// Free[External, A]

