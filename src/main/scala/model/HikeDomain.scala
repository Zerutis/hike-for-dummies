package dummies
package model

import zio.json._

case class HikeDomain(
  id: Int,
  name: String,
  distance: Double,
  elevation: Int,
  difficulty: String,
  description: String,
)

object HikeDomain {
  implicit val encoder: JsonEncoder[HikeDomain] = DeriveJsonEncoder.gen[HikeDomain]
  implicit val decoder: JsonDecoder[HikeDomain] = DeriveJsonDecoder.gen[HikeDomain]
}
