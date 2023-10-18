package dummies
package clients

import model.Hike
import protos.hikes.ZioHikes.HikeServiceClient
import protos.hikes.{GetHikeRequest, GetHikeResponse}

import io.grpc.{ManagedChannelBuilder, StatusException}
import scalapb.zio_grpc.ZManagedChannel
import zio._

case class HikeClient() {
  def getHike: ZIO[HikeServiceClient, StatusException, Option[Hike]] =
    for {
      response <- HikeServiceClient.getHike(GetHikeRequest())
    } yield response.hike.map(hike => Hike(hike.id.toInt, "hike.name", 5.0, 1, "hike.difficulty", "hike.description"))
}

object HikeClient {
  val clientLayer: Layer[Throwable, HikeServiceClient] =
    HikeServiceClient.live(
      ZManagedChannel(
        ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext()
      )
    )

  def getHike: ZIO[HikeServiceClient, StatusException, Option[Hike]] = {
    for {
      response <- HikeServiceClient.getHike(GetHikeRequest("1"))
    } yield response.hike.map(hike => Hike(hike.id.toInt, "hike.name", 5.0, 1, "hike.difficulty", "hike.description"))
  }
}
