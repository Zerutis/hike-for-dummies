package dummies
package controller

import model.Hike._
import service.HikeService

import zio._
import zio.http._
import zio.json._

object HikeController {
  def app: Http[HikeService, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> Root / "hikes" =>
      ZIO.serviceWithZIO[HikeService](_.listHikes())
        .map(hikes => Response.json(hikes.toJson))
  }
}
