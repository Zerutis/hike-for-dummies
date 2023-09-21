package dummies

import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import zio._
import zio.http._

import controller.HikeController
import repo.HikeRepo

object Main extends ZIOAppDefault {

  lazy val postgresLayer = Quill.Postgres.fromNamingStrategy(SnakeCase)
  lazy val dataSourceLayer = Quill.DataSource.fromPrefix("ctx")

  lazy val databaseLayer = dataSourceLayer >>> postgresLayer
  lazy val hikeLayer =  databaseLayer >>>  HikeRepo.layer

  lazy val apps = HikeController.app
  lazy val httpApps = apps.provideLayer(hikeLayer).withDefaultErrorResponse

  override def run = for {
    _ <- Console.printLine(s"Starting server at http://localhost:8080")
    server <- Server.serve(httpApps).provide(Server.default)
  } yield server
}