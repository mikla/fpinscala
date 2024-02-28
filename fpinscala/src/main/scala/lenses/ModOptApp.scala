package lenses

import monocle.{Focus, Lens}
import cats.implicits._

object ModOptApp extends App {

  case class Device(
    geo: Option[Geo] = None)

  object Device {
    object lens {
      val deviceLat: Lens[Geo, Option[Double]] = Focus[Geo](_.lat)
      val deviceLon: Lens[Geo, Option[Double]] = Focus[Geo](_.lon)
      val deviceType: Lens[Geo, Option[String]] = Focus[Geo](_.`type`)
    }

  }

  case class Geo(
    lat: Option[Double] = None,
    lon: Option[Double] = None,
    `type`: Option[String] = None,
    country: Option[String] = None,
    region: Option[String] = None,
    city: Option[String] = None,
    zip: Option[String] = None)

  case class GeoData(
    lat: Option[Double] = None,
    lon: Option[Double] = None)

  val geoData = GeoData(Some(1d), Some(2d))

  val originalGeo: Geo = Geo(Some(10d), Some(20d))

  val latUpdate = Device.lens.deviceLat.modify(_.filter(_ != 0.0).orElse(geoData.lat))
  val lonUpdate = Device.lens.deviceLon.modify(_.filter(_ != 0.0).orElse(geoData.lon))

  val typeUpdate = Device.lens.deviceType.replace {
    (originalGeo.lat.filter(_ != 0.0), originalGeo.lon.filter(_ != 0.0))
      .mapN((_, _) => "UserProvided")
      .getOrElse("IP")
      .some
  }

  val mod = latUpdate >>> lonUpdate >>> typeUpdate

  println(mod(originalGeo))

}
