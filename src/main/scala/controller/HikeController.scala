package dummies
package controller

import model.Hike
import model.Hike._
import repo.HikeRepo

import zio._
import zio.http.HttpError._
import zio.http._
import zio.json._

object HikeController {
  def app: Http[HikeRepo, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> Root / "hikes" =>
      ZIO.serviceWithZIO[HikeRepo](_.findAll)
        .map(hikes => Response.json(hikes.toJson))
    case Method.GET -> Root / "hikes" / int(id) =>
      ZIO.serviceWithZIO[HikeRepo](_.findById(id))
        .map(hike => Response.json(hike.toJson))
    case request @ Method.POST -> Root / "hikes" =>
      for {
        memberJson <- request.body.asString
        hike <- ZIO.fromEither(memberJson.fromJson[Hike]).mapError(msg => BadRequest(msg))
        _ <- ZIO.serviceWithZIO[HikeRepo](_.insert(hike))
      } yield Response.json(hike.toJson)
    case request @ Method.PUT -> Root / "hikes" =>
      for {
        memberJson <- request.body.asString
        hike <- ZIO.fromEither(memberJson.fromJson[Hike]).mapError(msg => BadRequest(msg))
        _ <- ZIO.serviceWithZIO[HikeRepo](_.update(hike))
      } yield Response.json(hike.toJson)
    case Method.DELETE -> Root / "hikes" / int(id) =>
      ZIO.serviceWithZIO[HikeRepo](_.delete(id))
        .map(id => Response.text(s"Hike deleted with id: $id"))
  }

  def viewApp: Http[HikeRepo, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> Root / "mvc" / "hikes" =>
      for {
        hikes <- ZIO.serviceWithZIO[HikeRepo](_.findAll)
      } yield Response.html(View.index(hikes))
    case Method.GET -> Root / "mvc" / "hikes" / int(id) =>
      ZIO.serviceWithZIO[HikeRepo](_.findById(id))
        .map(hike => Response.html(s"<p>${hike.get.name}</p>"))
    case request @ Method.POST -> Root / "mvc" / "hikes" =>
      for {
        body <- request.body.asString(Charsets.Utf8)
        _ <- ZIO.logInfo(body)
//        memberJson <- request.body.asString
//        hike <- ZIO.fromEither(memberJson.fromJson[Hike]).mapError(msg => BadRequest(msg))
//        _ <- ZIO.serviceWithZIO[HikeRepo](_.insert(hike))
        hikes <- ZIO.serviceWithZIO[HikeRepo](_.findAll)
      } yield Response.html(View.index(hikes))
  }
}
