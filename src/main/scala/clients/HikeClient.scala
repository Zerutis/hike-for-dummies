package dummies
package clients

import model._
import protos.hikes.ZioHikes.HikeServiceClient
import protos.hikes._

import io.grpc.{ManagedChannelBuilder, StatusException}
import scalapb.zio_grpc.ZManagedChannel
import zio._

object HikeClient {
  val clientLayer: TaskLayer[HikeServiceClient] =
    HikeServiceClient.live(
      ZManagedChannel(
        ManagedChannelBuilder.forAddress("localhost", 9000).usePlaintext()
      )
    )

  def getHike(id: Int): ZIO[HikeServiceClient, StatusException, Option[HikeDomain]] = {
    for {
      response <- HikeServiceClient.getHike(GetHikeRequest(id))
      hike = response.hike.map(toDomain)
    } yield hike
  }

  def getHikes: ZIO[HikeServiceClient, StatusException, List[HikeDomain]] = {
    for {
      response <- HikeServiceClient.getHikes(GetHikesRequest())
      hikes = response.hikes.map(toDomain).toList
    } yield hikes
  }

  def createHike(hike: HikeDomain): ZIO[HikeServiceClient, StatusException, Int] = {
    val request = CreateHikeRequest(Some(toProto(hike)))

    for {
      response <- HikeServiceClient.createHike(request)
    } yield response.id
  }

  def updateHike(hike: HikeDomain): ZIO[HikeServiceClient, StatusException, Int] = {
    val request = UpdateHikeRequest(Some(toProto(hike)))

    for {
      response <- HikeServiceClient.updateHike(request)
    } yield response.id
  }

  def deleteHike(id: Int): ZIO[HikeServiceClient, StatusException, Unit] = {
    for {
      _ <- HikeServiceClient.deleteHike(DeleteHikeRequest(id))
    } yield ()
  }

  private def toProto(hike: HikeDomain): Hike =
    Hike(hike.id, hike.name, hike.distance, hike.elevation, hike.difficulty, hike.description)

  private def toDomain(hike: Hike): HikeDomain =
    HikeDomain(hike.id, hike.name, hike.distance, hike.elevation, hike.difficulty, hike.description)
}
