package dummies
package mvc

import model.HikeDomain
import model.HikeDomain._
import repo.HikeRepo

import zio._
import zio.http.HttpError._
import zio.http._
import zio.json._

class HikeControllerAsMVC {
  def viewApp: Http[HikeRepo, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> Root / "mvc" / "hikes" / int(id) =>
      for {
        hike <- ZIO.serviceWithZIO[HikeRepo](_.findById(id)).map(_.get)
      } yield Response.html(HikeView.editForm(hike))
    case Method.GET -> Root / "mvc" / "index" =>
      for {
        hikes <- ZIO.serviceWithZIO[HikeRepo](_.findAll)
      } yield Response.html(HikeView.index(hikes))
    case Method.GET -> Root / "mvc" / "hikes" =>
      for {
        hikes <- ZIO.serviceWithZIO[HikeRepo](_.findAll)
      } yield Response.html(HikeView.hikesTable(hikes))
    case request @ Method.POST -> Root / "mvc" / "hikes" =>
      for {
        memberJson <- request.body.asString
        hike <- ZIO.fromEither(memberJson.fromJson[HikeDomain]).mapError(msg => BadRequest(msg))
        _ <- ZIO.serviceWithZIO[HikeRepo](_.insert(hike))
        hikes <- ZIO.serviceWithZIO[HikeRepo](_.findAll)
      } yield Response.html(HikeView.hikesTable(hikes))
    case request @ Method.PUT -> Root / "mvc" / "hikes" =>
      for {
        memberJson <- request.body.asString
        hike <- ZIO.fromEither(memberJson.fromJson[HikeDomain]).mapError(msg => BadRequest(msg))
        _ <- ZIO.serviceWithZIO[HikeRepo](_.update(hike))
        hikes <- ZIO.serviceWithZIO[HikeRepo](_.findAll)
      } yield Response.html(HikeView.index(hikes))
    case Method.DELETE -> Root / "mvc" / "hikes" / int(id) =>
      for {
        _ <- ZIO.serviceWithZIO[HikeRepo](_.delete(id))
      } yield Response.ok
  }
}
