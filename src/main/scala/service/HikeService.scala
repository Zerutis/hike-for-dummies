package dummies
package service

import repo.HikeRepo

import zio.{ZIO, ZLayer}

case class HikeService(hikeRepo: HikeRepo) {
  def getHike(id: Int) = hikeRepo.findById(id)
}

object HikeService {
  val layer = ZLayer(ZIO.service[HikeRepo].map(HikeService(_)))
}
