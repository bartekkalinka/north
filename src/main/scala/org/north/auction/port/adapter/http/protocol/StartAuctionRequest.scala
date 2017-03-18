package org.north.auction.port.adapter.http.protocol

import java.util.UUID

import org.north.auction.domain.model.{Auction, Bid, Product, User}
import org.north.auction.port.adapter.http.protocol.AuctionRequest.InvalidRequestReason
import org.north.auction.util.TimeUtils

case object EmptySellerName extends InvalidRequestReason { val message = "seller name is empty" }
case object EmptyProductName extends InvalidRequestReason { val message = "product name is empty" }

case class StartAuctionRequest(seller: User, product: Product) {
  def prepareStartAuction: Either[InvalidRequestReason, Auction] = {
    if(seller.name == "") {
      Left(EmptySellerName)
    } else if(product.name == "") {
      Left(EmptyProductName)
    } else {
      val time = TimeUtils()
      val auction = Auction(
        id = UUID.randomUUID().toString,
        seller = seller,
        product = product,
        highestBid = Bid(seller, 0, time.current),
        expires = time.daysFromNow(1)
      )
      Right(auction)
    }
  }
}

