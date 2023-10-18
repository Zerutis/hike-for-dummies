package dummies
package controller

import clients.HikeClient
import model.Hike
import model.Hike._
import repo.HikeRepo

import io.grpc.StatusException
import protos.hikes.ZioHikes.HikeServiceClient
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

  def grpcApp: Http[HikeServiceClient, StatusException, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> Root / "grpc" / "hikes" =>
      HikeClient.getHike.map(hike => Response.json(hike.toJson))
  }
}
