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
      test("Should return hike when one exists") {
        val expectedHike = Hike(1, "Hike 1", 10.5, 15, "Easy", "Hike 1 description")

        for {
          _ <- HikeRepo.insert(expectedHike)
          response <- app.runZIO(Request.get(url = URL(Root / "hikes" / expectedHike.id.toString)))
          actualHike <- response.body.asString.map(_.fromJson[Hike].toOption)
        } yield assertTrue(Option(expectedHike) == actualHike)
      },

      test("Should return not found exception when no hike exists") {
        for {
          response <- app.runZIO(Request.get(url = URL(Root / "hikes" / "1")))
          actualHike <- response.body.asString.map(_.fromJson[Hike].toOption)
        } yield assertTrue(actualHike.isEmpty)
      }
    ).provide(InMemoryHikeRepo.layer)
}
