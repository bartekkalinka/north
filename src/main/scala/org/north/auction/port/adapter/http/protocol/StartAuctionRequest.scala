package org.north.auction.port.adapter.http.protocol

import org.north.auction.domain.AuctionActor.StartAuction
import org.north.auction.domain.model.{Auction, Bid, Product, User}
import org.north.auction.util.TimeUtils

case class StartAuctionRequest(seller: User, product: Product) {
  def prepareStartAuction: StartAuction = {
    val time = TimeUtils()
    StartAuction(Auction(
      seller = seller,
      product = product,
      highestBid = Bid(seller, 0, time.current),
      expires = time.daysFromNow(1)
    ))
  }
}

