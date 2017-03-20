package org.north.auction.port.adapter.http.protocol

import org.north.auction.domain.AuctionActor.BidInAuction
import org.north.auction.domain.model.User

case class BidRequest(bidder: User, amount: Int) {
  def toBidInAuction: BidInAuction = BidInAuction(bidder, amount)
}

