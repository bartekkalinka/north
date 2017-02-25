package org.north.auction.port.adapter.http

import org.north.auction.domain.model.{Auction, Bid, Product, User}
import spray.json.DefaultJsonProtocol

trait JsonProtocol extends DefaultJsonProtocol {
  implicit val userFormat = jsonFormat1(User.apply)
  implicit val bidFormat = jsonFormat3(Bid.apply)
  implicit val productFormat = jsonFormat1(Product.apply)
  implicit val auctionFormat = jsonFormat4(Auction.apply)
}

