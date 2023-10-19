package dummies
package repo

import model.HikeDomain

import zio.{Ref, Task, ZLayer}

import scala.collection.mutable

case class InMemoryHikeRepo(hikes: Ref[mutable.Map[Int, HikeDomain]]) extends HikeRepo {
  override def findAll: Task[List[HikeDomain]] = hikes.get.map(_.values.toList)

  override def findById(id: Int): Task[Option[HikeDomain]] = hikes.get.map(_.get(id))

  override def insert(hike: HikeDomain): Task[Long] = hikes.updateAndGet(_.addOne(hike.id, hike)).map(_.size)

  override def update(hike: HikeDomain): Task[Long] = hikes.updateAndGet(_.addOne(hike.id, hike)).map(_.size)

  override def delete(id: Int): Task[Long] = hikes.updateAndGet(hike => {
    hike.remove(id).map(_ => mutable.Map.empty[Int, HikeDomain]).get
  }).map(_ => id)
}

object InMemoryHikeRepo {
  val layer: zio.ZLayer[Any, Nothing, InMemoryHikeRepo] = ZLayer.fromZIO(
    Ref.make(mutable.Map.empty[Int, HikeDomain]).map(new InMemoryHikeRepo(_))
  )
}