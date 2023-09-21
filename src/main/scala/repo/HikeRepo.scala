package dummies
package repo

import model.Hike

import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import zio._

case class HikeRepo(quill: Quill.Postgres[SnakeCase]) {
  import quill._

  private val hikes = quote(query[Hike])

  def findAll: Task[List[Hike]] =
    run(hikes)

  def findById(id: Int): Task[Option[Hike]] =
    run(hikes.filter(_.id == lift(id))).map(_.headOption)

  def insert(hike: Hike): Task[Long] =
    run(hikes.insertValue(lift(hike)))

  def update(hike: Hike): Task[Long] = run(
    hikes
      .filter(hikeDomain => hikeDomain.id == lift(hike.id))
      .updateValue(lift(hike))
    )

  def delete(id: Int): Task[Long] =
    run(hikes.filter(_.id == lift(id)).delete)
}

object HikeRepo {
  val layer: ZLayer[Quill.Postgres[SnakeCase], Nothing, HikeRepo] = ZLayer.fromFunction(HikeRepo(_))
}