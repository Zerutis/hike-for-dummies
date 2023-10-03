package dummies
package repo

import model.Hike

import zio.{Task, ZIO}

trait HikeRepo {
  def findAll: Task[List[Hike]]

  def findById(id: Int): Task[Option[Hike]]

  def insert(hike: Hike): Task[Long]

  def update(hike: Hike): Task[Long]

  def delete(id: Int): Task[Long]
}

object HikeRepo {
  def findAll: ZIO[HikeRepo, Throwable, List[Hike]] =
    ZIO.serviceWithZIO[HikeRepo](_.findAll)

  def findById(id: Int): ZIO[HikeRepo, Throwable, Option[Hike]] =
    ZIO.serviceWithZIO[HikeRepo](_.findById(id))

  def insert(hike: Hike): ZIO[HikeRepo, Throwable, Long] =
    ZIO.serviceWithZIO[HikeRepo](_.insert(hike))

  def update(hike: Hike): ZIO[HikeRepo, Throwable, Long] =
    ZIO.serviceWithZIO[HikeRepo](_.update(hike))

  def delete(id: Int): ZIO[HikeRepo, Throwable, Long] =
    ZIO.serviceWithZIO[HikeRepo](_.delete(id))
}
