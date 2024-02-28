package cvlv

import io.circe.generic.auto._
import sttp.client4.circe._
import sttp.client4._

import scala.concurrent.duration.Duration

object MatchCategoriesApp extends App {

  case class VacancyPreview(id: Long, categories: List[Int])
  case class VacancyFull(id: Long, settings: Settings)
  case class Settings(categories: List[String])

  case class ListPayload(
    vacancies: List[VacancyPreview])

  val backend: SyncBackend = DefaultSyncBackend()

  def listVacancies(): ListPayload = {
    val response: Response[Either[ResponseException[String, io.circe.Error], ListPayload]] =
      basicRequest
        .get(uri"${listUrl()}")
        .response(asJson[ListPayload])
        .readTimeout(Duration.Inf)
        .send(backend)

    response.body.toOption.get
  }

  def fetchDescription(id: Long) = {
    val response: Response[Either[ResponseException[String, io.circe.Error], VacancyFull]] =
      basicRequest
        .get(uri"${viewUrl(id)}")
        .response(asJson[VacancyFull])
        .readTimeout(Duration.Inf)
        .send(backend)

    response.body.toOption.get
  }

  def listUrl() =
    "https://cv.lv/api/v1/vacancy-search-service/search?limit=100&offset=100&categories[]=INFORMATION_TECHNOLOGY&towns[]=&fuzzy=true&suitableForRefugees=false&isHourlySalary=false&isRemoteWork=false&isQuickApply=false"

  def viewUrl(id: Long) =
    s"https://cv.lv/api/v1/vacancies-service/$id/public"

  def matchCategories(pairs: List[(List[Int], List[String])]): Map[Int, String] = {
    val corr = Map.empty[Int, String]

    pairs.foldLeft(corr) { case (map, (ids, names)) =>
      val localM = ids.zip(names).foldLeft(corr) { case (m, (id, name)) =>
        val categoryName = m.get(id)
        categoryName.foreach(value => if (value != name) println("Found category with same id but different name"))
        if (categoryName.isEmpty) m + (id -> name) else m
      }

      map ++ localM
    }
  }

  val idCatPairs = listVacancies().vacancies.map { v =>
    Thread.sleep(1000)
    v.categories -> fetchDescription(v.id).settings.categories
  }

  val categories = matchCategories(idCatPairs)

  categories.toList.sortBy(_._1).foreach { case (id, name) =>
    println(s"$id,$name")
  }

}
