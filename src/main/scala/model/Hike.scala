package dummies
package model

import zio.json._

case class Hike(
  id: Int,
  name: String,
  distance: Double,
  elevation: Int,
  difficulty: String,
  description: String,
)

object Hike {
  implicit val encoder: JsonEncoder[Hike] = DeriveJsonEncoder.gen[Hike]
  implicit val decoder: JsonDecoder[Hike] = DeriveJsonDecoder.gen[Hike]
}
