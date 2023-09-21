package dummies
package repo

import model.Hike

import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import zio._

case class HikeRepo(quill: Quill.Postgres[SnakeCase]) {
  import quill._

  private val hikes = quote(query[Hike])

  def findAll(): Task[List[Hike]] = {
    run(hikes)
  }
}

object HikeRepo {
  val layer: ZLayer[Quill.Postgres[SnakeCase], Nothing, HikeRepo] = ZLayer.fromFunction(HikeRepo(_))
}