package org.north.auction.domain

import akka.actor.{Actor, ActorLogging, Props}
import org.north.auction.domain.AuctionActor.StartAuction
import org.north.auction.domain.model._
import org.north.auction.util.TimeUtils

object AuctionActor {
  trait Command
  case class StartAuction(id: String, seller: User, product: Product, expiresIn: Long) {
    def toAuction: Auction = {
      val time = TimeUtils()
      Auction(
        id = this.id,
        seller = this.seller,
        product = this.product,
        highestBid = Bid(this.seller, 0, time.current),
        expires = time.current + expiresIn,
        expired = false
      )
    }
  }
  case object GetAuction extends Command
  case class BidInAuction(bidder: User, amount: Int) extends Command
  def props(startAuction: StartAuction): Props = Props(new AuctionActor(startAuction))
}

class AuctionActor(startAuction: StartAuction) extends Actor with ActorLogging {
  import AuctionActor._
  import context._

  def receive: Receive = handleAuction(startAuction.toAuction)

  def handleAuction(auction: Auction): Receive = {
    case GetAuction =>
      val newAuction = auction.updateExpired(TimeUtils().current)
      sender ! newAuction
      become(handleAuction(newAuction))
    case BidInAuction(bidder, amount) =>
      val time = TimeUtils()
      val updatedAuction = auction.updateExpired(time.current)
      val bid = Bid(bidder, amount, time.current)
      updatedAuction.bid(bid) match {
        case result@Right(auctionAfterBid) =>
          sender ! result
          become(handleAuction(auctionAfterBid))
        case result@Left(failureReason) =>
          sender ! result
          become(handleAuction(updatedAuction))
      }
  }
}