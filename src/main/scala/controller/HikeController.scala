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
      HikeRepo.findAll.map(hikes => Response.json(hikes.toJson))
    case Method.GET -> Root / "hikes" / int(id) =>
      for {
        maybeHike <- HikeRepo.findById(id)
      } yield maybeHike match {
        case Some(hike) => Response.json(hike.toJson)
        case None => Response.status(Status.NotFound)
      }
    case request @ Method.POST -> Root / "hikes" =>
      for {
        memberJson <- request.body.asString
        hike <- ZIO.fromEither(memberJson.fromJson[Hike]).mapError(msg => BadRequest(msg))
        _ <- HikeRepo.insert(hike)
      } yield Response.json(hike.toJson)
    case request @ Method.PUT -> Root / "hikes" =>
      for {
        memberJson <- request.body.asString
        hike <- ZIO.fromEither(memberJson.fromJson[Hike]).mapError(msg => BadRequest(msg))
        _ <- HikeRepo.update(hike)
      } yield Response.json(hike.toJson)
    case Method.DELETE -> Root / "hikes" / int(id) =>
      HikeRepo.delete(id).map(id => Response.text(s"Hike deleted with id: $id"))
  }

  def viewApp: Http[HikeRepo, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> Root / "mvc" / "hikes" / int(id) =>
      for {
        hike <- ZIO.serviceWithZIO[HikeRepo](_.findById(id)).map(_.get)
      } yield Response.html(View.editForm(hike))
    case Method.GET -> Root / "mvc" / "index" =>
      for {
        hikes <- ZIO.serviceWithZIO[HikeRepo](_.findAll)
      } yield Response.html(View.index(hikes))
    case Method.GET -> Root / "mvc" / "hikes" =>
      for {
        hikes <- ZIO.serviceWithZIO[HikeRepo](_.findAll)
      } yield Response.html(View.hikesTable(hikes))
    case request @ Method.POST -> Root / "mvc" / "hikes" =>
      for {
        memberJson <- request.body.asString
        hike <- ZIO.fromEither(memberJson.fromJson[Hike]).mapError(msg => BadRequest(msg))
        _ <- ZIO.serviceWithZIO[HikeRepo](_.insert(hike))
        hikes <- ZIO.serviceWithZIO[HikeRepo](_.findAll)
      } yield Response.html(View.hikesTable(hikes))
    case request @ Method.PUT -> Root / "mvc" / "hikes" =>
      for {
        memberJson <- request.body.asString
        hike <- ZIO.fromEither(memberJson.fromJson[Hike]).mapError(msg => BadRequest(msg))
        _ <- ZIO.serviceWithZIO[HikeRepo](_.update(hike))
        hikes <- ZIO.serviceWithZIO[HikeRepo](_.findAll)
      } yield Response.html(View.index(hikes))
    case Method.DELETE -> Root / "mvc" / "hikes" / int(id) =>
      for {
        _ <- ZIO.serviceWithZIO[HikeRepo](_.delete(id))
      } yield Response.ok
  }
}
