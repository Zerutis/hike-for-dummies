package dummies

import model._
import repo.{HikeRepo, PersistentHikeRepo}

import protos.hikes.ZioHikes.HikeService
import protos.hikes._
import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import io.grpc.{ServerBuilder, StatusException}
import scalapb.zio_grpc.ServerLayer
import zio.{IO, Console, ZIO, ZIOAppDefault, ZLayer}

case class HikeServer(hikeRepo: HikeRepo) extends HikeService {
  override def getHike(request: GetHikeRequest): IO[StatusException, GetHikeResponse] = {
    for {
      maybeHikeDomain <- hikeRepo.findById(request.id)
        .mapError(e => new StatusException(io.grpc.Status.INTERNAL.withDescription(e.getMessage)))
      maybeHike = maybeHikeDomain.map(hike => toProto(hike))
    } yield GetHikeResponse(maybeHike)
  }

  override def getHikes(request: GetHikesRequest): IO[StatusException, GetHikesResponse] =
    for {
      hikes <- hikeRepo.findAll
        .mapError(e => new StatusException(io.grpc.Status.INTERNAL.withDescription(e.getMessage)))
    } yield GetHikesResponse(hikes.map(toProto))

  override def createHike(request: CreateHikeRequest): IO[StatusException, CreateHikeResponse] =
    for {
      hikeDomain <- ZIO.fromOption(request.hike.map(toDomain))
        .mapError(_ => new StatusException(io.grpc.Status.INVALID_ARGUMENT.withDescription("Invalid Hike")))
      hikeId <- hikeRepo.insert(hikeDomain)
      .mapError(e => new StatusException(io.grpc.Status.INTERNAL.withDescription(e.getMessage)))
    } yield CreateHikeResponse(hikeId.toInt)

  override def updateHike(request: UpdateHikeRequest): IO[StatusException, UpdateHikeResponse] =
    for {
      hikeDomain <- ZIO.fromOption(request.hike.map(toDomain))
        .mapError(_ => new StatusException(io.grpc.Status.INVALID_ARGUMENT.withDescription("Invalid Hike")))
      hikeId <- hikeRepo.update(hikeDomain)
        .mapError(e => new StatusException(io.grpc.Status.INTERNAL.withDescription(e.getMessage)))
    } yield UpdateHikeResponse(hikeId.toInt)

  override def deleteHike(request: DeleteHikeRequest): IO[StatusException, DeleteHikeResponse] =
    for {
      _ <- hikeRepo.delete(request.id)
        .mapError(e => new StatusException(io.grpc.Status.INTERNAL.withDescription(e.getMessage)))
    } yield DeleteHikeResponse()

  private def toProto(hike: HikeDomain): Hike =
    Hike(hike.id, hike.name, hike.distance, hike.elevation, hike.difficulty, hike.description)

  private def toDomain(hike: Hike): HikeDomain =
    HikeDomain(hike.id, hike.name, hike.distance, hike.elevation, hike.difficulty, hike.description)
}

object HikeServer extends ZIOAppDefault{
  val dataSourceLayer = Quill.DataSource.fromPrefix("ctx")
  val postgresLayer = Quill.Postgres.fromNamingStrategy(SnakeCase)

  val hikeServerLayer = ZLayer.fromFunction(HikeServer(_))

  val hikeServiceLayer = ZLayer.make[HikeService](
      dataSourceLayer,
      postgresLayer,
      PersistentHikeRepo.layer,
      hikeServerLayer,
    )

  def welcome: ZIO[Any, Throwable, Unit] =
    Console.printLine("Server is running on port 9000. Press Ctrl-C to stop.")

  def grpcServices = ServerLayer.fromServiceLayer(ServerBuilder.forPort(9000))(hikeServiceLayer)

  override def run = welcome *> grpcServices.build *> ZIO.never
}