package org.north.auction.port.adapter.http

import org.north.auction.domain.model.{Auction, Bid, Product, User}
import org.north.auction.port.adapter.http.protocol.{BidRequest, StartAuctionRequest}
import spray.json.DefaultJsonProtocol

object JsonProtocol extends DefaultJsonProtocol {
  implicit val userFormat = jsonFormat1(User.apply)
  implicit val productFormat = jsonFormat1(Product.apply)
  implicit val startAuctionRequestFormat = jsonFormat2(StartAuctionRequest)
  implicit val bidFormat = jsonFormat3(Bid)
  implicit val bidRequestFormat = jsonFormat2(BidRequest)
  implicit val auctionFormat = jsonFormat6(Auction)
}

