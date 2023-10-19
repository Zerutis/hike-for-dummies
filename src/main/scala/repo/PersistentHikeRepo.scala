package dummies
package repo

import model.HikeDomain

import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import zio._

case class PersistentHikeRepo(quill: Quill.Postgres[SnakeCase]) extends HikeRepo {
  import quill._

  private val hikes = quote(query[HikeDomain])

  override def findAll: Task[List[HikeDomain]] =
    run(hikes)

  override def findById(id: Int): Task[Option[HikeDomain]] =
    run(hikes.filter(_.id == lift(id))).map(_.headOption)

  override def insert(hike: HikeDomain): Task[Long] =
    run(hikes.insertValue(lift(hike)))

  override def update(hike: HikeDomain): Task[Long] = run(
    hikes
      .filter(hikeDomain => hikeDomain.id == lift(hike.id))
      .updateValue(lift(hike))
  )

  override def delete(id: Int): Task[Long] =
    run(hikes.filter(_.id == lift(id)).delete)
}

object PersistentHikeRepo {
  val layer: ZLayer[Quill.Postgres[SnakeCase], Nothing, PersistentHikeRepo] = ZLayer.fromFunction(PersistentHikeRepo(_))
}