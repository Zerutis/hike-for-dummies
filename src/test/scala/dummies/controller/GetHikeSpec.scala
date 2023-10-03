package dummies
package controller


import model.Hike
import repo.{HikeRepo, InMemoryHikeRepo}

import zio.Scope
import zio.json._
import zio.http.{URL, _}
import zio.test._
import zio.test.ZIOSpecDefault

object GetHikeSpec extends ZIOSpecDefault {

  val app: Http[HikeRepo, Throwable, Request, Response] = HikeController.app

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Get hike")(
      test("Should return something") {
        for {
          _ <- HikeRepo.insert(Hike(2, "Hike 1", 10.5, 15, "Easy", "Hike 1 description"))
          response <- app.runZIO(Request.get(url = URL(Root / "hikes" / "1")))
          hike <- response.body.asString.map(_.fromJson[Hike].toOption)
        } yield assertTrue(hike.get.id == 1)
      }
    ).provide(InMemoryHikeRepo.layer)
}
