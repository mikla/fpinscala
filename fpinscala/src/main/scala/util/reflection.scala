package util

object reflection {

  def safeSimpleName[A](a: A): String = {
    val clazz = a.getClass
    try clazz.getSimpleName
    catch {
      case _: Throwable =>
        val name = clazz.getName
        name.substring(name.lastIndexOf(".") + 1)
    }
  }

  def safeNameTrimmed[A](a: A): String = safeSimpleName(a).replace("$", "").trim

}
