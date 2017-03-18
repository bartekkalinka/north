package org.north.auction.port.adapter.http.protocol

object AuctionRequest {
  trait InvalidRequestReason {
    def message: String
  }
}

trait AuctionRequest

