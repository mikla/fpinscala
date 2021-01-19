package stuff

object MultiplePageStateApp extends App {

  trait StatusSummary

  sealed trait PageState {
    def userMenuOpened: Boolean
    def mobileMenuOpened: Boolean
    def setUserMenuOpened(opened: Boolean): PageState
    def swapMobileMenuOpened: PageState
  }

  object PageState {

    @AutoMenu
    case class RootPage(
      promotedStatuts: Seq[StatusSummary],
      override val userMenuOpened: Boolean = false,
      override val mobileMenuOpened: Boolean = false
    ) extends PageState

    @AutoMenu
    case class SettingsPage(
      override val userMenuOpened: Boolean = false,
      override val mobileMenuOpened: Boolean = false
    ) extends PageState

  }

  PageState.SettingsPage().swapMobileMenuOpened

}
