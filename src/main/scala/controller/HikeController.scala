package dummies
package controller

import model.Hike._
import service.HikeService

import zio._
import zio.http._
import zio.json._

object HikeController {
  def app: Http[HikeService, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> Root / "hikes" / int(id) =>
      ZIO.serviceWithZIO[HikeService](_.getHike(id))
        .map(hikes => Response.json(hikes.toJson))
    case Method.GET -> Root / "hello" =>
      ZIO.succeed(Response.text("Hello, World!"))
  }
}