package dummies
package service

import repo.HikeRepo

import model.Hike
import zio.{Task, ZIO, ZLayer}

case class HikeService(hikeRepo: HikeRepo) {
  def listHikes(): Task[List[Hike]] = hikeRepo.findAll()
}

object HikeService {
  val layer = ZLayer(ZIO.service[HikeRepo].map(HikeService(_)))
}
