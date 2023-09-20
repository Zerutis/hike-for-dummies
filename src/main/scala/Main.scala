package dummies

import zio._
import zio.http._
import controller.HikeController
import repo.HikeRepo

import dummies.service.HikeService
import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill

object Main extends ZIOAppDefault {

  private val h2Layer = Quill.H2.fromNamingStrategy(SnakeCase)
  private val dataSourceLayer = Quill.DataSource.fromPrefix("ctx")

  private val databaseLayer = dataSourceLayer >>> h2Layer
  private val hikeLayer = databaseLayer >>> HikeRepo.layer >>> HikeService.layer

  private val apps = HikeController.app
  private val httpApps = apps.provideLayer(hikeLayer).withDefaultErrorResponse

  override def run = Server.serve(httpApps).provide(Server.defaultWithPort(8080))
}