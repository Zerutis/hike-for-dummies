package dummies
package repo

import model.Hike

import zio.{Ref, Task, ZLayer}

import scala.collection.mutable

case class InMemoryHikeRepo(hikes: Ref[mutable.Map[Int, Hike]]) extends HikeRepo {
  override def findAll: Task[List[Hike]] = hikes.get.map(_.values.toList)

  override def findById(id: Int): Task[Option[Hike]] = hikes.get.map(_.get(id))

  override def insert(hike: Hike): Task[Long] = hikes.updateAndGet(_.addOne(hike.id, hike)).map(_.size)

  override def update(hike: Hike): Task[Long] = hikes.updateAndGet(_.addOne(hike.id, hike)).map(_.size)

  override def delete(id: Int): Task[Long] = hikes.updateAndGet(hike => {
    hike.remove(id).map(_ => mutable.Map.empty[Int, Hike]).get
  }).map(_ => id)
}

object InMemoryHikeRepo {
  val layer: zio.ZLayer[Any, Nothing, InMemoryHikeRepo] = ZLayer.fromZIO(
    Ref.make(mutable.Map.empty[Int, Hike]).map(new InMemoryHikeRepo(_))
  )
}