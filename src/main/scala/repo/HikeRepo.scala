package dummies
package repo

import model.HikeDomain

import zio.{Task, ZIO}

trait HikeRepo {
  def findAll: Task[List[HikeDomain]]

  def findById(id: Int): Task[Option[HikeDomain]]

  def insert(hike: HikeDomain): Task[Long]

  def update(hike: HikeDomain): Task[Long]

  def delete(id: Int): Task[Long]
}

object HikeRepo {
  def findAll: ZIO[HikeRepo, Throwable, List[HikeDomain]] =
    ZIO.serviceWithZIO[HikeRepo](_.findAll)

  def findById(id: Int): ZIO[HikeRepo, Throwable, Option[HikeDomain]] =
    ZIO.serviceWithZIO[HikeRepo](_.findById(id))

  def insert(hike: HikeDomain): ZIO[HikeRepo, Throwable, Long] =
    ZIO.serviceWithZIO[HikeRepo](_.insert(hike))

  def update(hike: HikeDomain): ZIO[HikeRepo, Throwable, Long] =
    ZIO.serviceWithZIO[HikeRepo](_.update(hike))

  def delete(id: Int): ZIO[HikeRepo, Throwable, Long] =
    ZIO.serviceWithZIO[HikeRepo](_.delete(id))
}
