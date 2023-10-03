package dummies

import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import zio._
import zio.http._
import controller.HikeController
import repo.PersistentHikeRepo

import zio.http.Header.{AccessControlAllowOrigin, Origin}
import zio.http.HttpAppMiddleware.cors
import zio.http.internal.middlewares.Cors.CorsConfig
import zio.logging.backend.SLF4J

object Main extends ZIOAppDefault {

  override val bootstrap = Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  lazy val corsConfig: CorsConfig = CorsConfig(
    allowedOrigin = {
      case origin@Origin.Value(_, host, _) if host == "localhost" => Some(AccessControlAllowOrigin.Specific(origin))
      case _ => None
    }
  )

  lazy val postgresLayer = Quill.Postgres.fromNamingStrategy(SnakeCase)
  lazy val dataSourceLayer = Quill.DataSource.fromPrefix("ctx")

  lazy val databaseLayer = dataSourceLayer >>> postgresLayer
  lazy val hikeLayer =  databaseLayer >>>  PersistentHikeRepo.layer

  lazy val apps = HikeController.app ++ HikeController.viewApp @@ cors(corsConfig)
  lazy val httpApps = apps
    .provideLayer(hikeLayer)
    .mapError(e => Response.text(e.getMessage))

  override def run = for {
    _ <- Console.printLine(s"Starting server at http://localhost:8080")
    server <- Server.serve(httpApps).provide(Server.default).orDie
  } yield server
}