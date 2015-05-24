package actor.linker

import java.util.concurrent.Executor

import com.ning.http.client.AsyncHttpClient
import org.jsoup.Jsoup

import scala.concurrent.{Promise, Future}
import scala.collection.JavaConverters._

object WebClient {

  val client = new AsyncHttpClient
  def get(url: String)(implicit exec: Executor): Future[String] = {
    val f = client.prepareGet(url).execute()
    val p = Promise[String]()

    f.addListener(new Runnable {
      override def run(): Unit = {
        val response = f.get
        if (response.getStatusCode < 400)
          p.success(response.getResponseBodyExcerpt(131072))
        else p.failure(new RuntimeException("Bad status"))
      }
    }, exec)
    p.future
  }
}

