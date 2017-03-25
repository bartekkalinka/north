package org.north.auction.port.adapter.http.protocol

import java.util.UUID

import org.north.auction.domain.AuctionActor.StartAuction
import org.north.auction.domain.model.{Product, User}
import org.north.auction.port.adapter.http.protocol.AuctionRequest.InvalidRequestReason

case object EmptySellerName extends InvalidRequestReason { val message = "seller name is empty" }
case object EmptyProductName extends InvalidRequestReason { val message = "product name is empty" }

case class StartAuctionRequest(seller: User, product: Product) {
  def prepareStartAuction: Either[InvalidRequestReason, StartAuction] = {
    if(seller.name == "") {
      Left(EmptySellerName)
    } else if(product.name == "") {
      Left(EmptyProductName)
    } else {
      val auction = StartAuction(
        id = UUID.randomUUID().toString,
        seller = seller,
        product = product
      )
      Right(auction)
    }
  }
}

