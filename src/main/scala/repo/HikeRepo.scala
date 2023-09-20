package dummies
package repo

import model.Hike

import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import zio._

case class HikeRepo(quill: Quill.H2[SnakeCase]) {
  import quill._

  private val hikes = quote(query[Hike])

  def findById(id: Int): Task[Option[Hike]] = {
    run(query[Hike].filter(_.id == lift(id))).map(_.headOption)
  }
}

object HikeRepo {
  val layer: ZLayer[Quill.H2[SnakeCase], Nothing, HikeRepo] = ZLayer.fromFunction(HikeRepo(_))
}