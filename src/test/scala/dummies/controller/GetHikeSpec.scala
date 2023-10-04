package dummies
package controller

import model.Hike
import model.Hike._
import repo.{HikeRepo, InMemoryHikeRepo}

import zio.Scope
import zio.json._
import zio.http._
import zio.test._
import zio.test.Assertion.equalTo
import zio.test.ZIOSpecDefault

object GetHikeSpec extends ZIOSpecDefault {

  val app: Http[HikeRepo, Throwable, Request, Response] = HikeController.app

  val hikeId = 1
  val request = Request.get(URL(Root / "hikes" / hikeId.toString))

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Get hike")(
      test("Should return response with hike") {
        val hike = Hike(hikeId, "Hike 1", 10.5, 15, "Easy", "Hike 1 description")

        for {
          _ <- HikeRepo.insert(hike)
          response <- app.runZIO(request)
        } yield assertTrue(response == Response.json(hike.toJson))
      },

      test("Should return not found when hike not found") {
        for {
          response <- app.runZIO(request)
        } yield assertTrue(response.status == Status.NotFound)
      },
    ).provide(InMemoryHikeRepo.layer)
}
