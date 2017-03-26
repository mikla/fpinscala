package stuff

import java.text.Normalizer.{ normalize ⇒ jnormalize, _ }

object StringNormalizerApp extends App {

  val latvianC = "āčēģīķļšūž"

  def normalize(in: String): String = {
    val normalized = jnormalize(in, Form.NFD)
    normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
  }

  println(normalize(latvianC))

}
