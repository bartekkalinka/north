package org.north.auction.domain

import akka.actor.{Actor, ActorLogging, Props}
import org.north.auction.domain.AuctionActor.StartAuction
import org.north.auction.domain.model.{Auction, Bid, Product, User}
import org.north.auction.util.TimeUtils

object AuctionActor {
  trait Command
  case class StartAuction(id: String, seller: User, product: Product) {
    def toAuction: Auction = {
      val time = TimeUtils()
      Auction(
        id = this.id,
        seller = this.seller,
        product = this.product,
        highestBid = Bid(this.seller, 0, time.current),
        expires = time.daysFromNow(1)
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

  val timeUtils = TimeUtils()

  def handleAuction(auction: Auction): Receive = {
    case GetAuction =>
      sender ! auction
    case BidInAuction(bidder, amount) =>
      val bid = Bid(bidder, amount, timeUtils.current)
      auction.bid(bid) match {
        case result@Right(newAuction) =>
          sender ! result
          become(handleAuction(newAuction))
        case result@Left(failureReason) =>
          sender ! result
      }
  }
}