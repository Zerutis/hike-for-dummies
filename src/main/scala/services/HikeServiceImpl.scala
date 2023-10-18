package dummies
package services

import protos.hikes.ZioHikes.HikeService
import protos.hikes.{GetHikeRequest, GetHikeResponse}

import io.grpc.StatusException
import scalapb.zio_grpc.{ServerMain, ServiceList}
import zio.{IO, ZIO}

object HikeServiceImpl extends HikeService {
  override def getHike(request: GetHikeRequest): IO[StatusException, GetHikeResponse] =
    ZIO.fail(new StatusException(io.grpc.Status.UNIMPLEMENTED))
}

object HikeServer extends ServerMain {
  def services: ServiceList[Any] = ServiceList.add(HikeServiceImpl)
}