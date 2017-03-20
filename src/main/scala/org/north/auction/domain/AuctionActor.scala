package org.north.auction.domain

import akka.actor.{Actor, ActorLogging, Props}
import org.north.auction.domain.model.{Auction, Bid, User}
import org.north.auction.util.TimeUtils

object AuctionActor {
  trait Command
  case object GetAuction extends Command
  case class BidInAuction(bidder: User, amount: Int) extends Command
  def props(auction: Auction): Props = Props(new AuctionActor(auction))
}

class AuctionActor(initAuction: Auction) extends Actor with ActorLogging {
  import AuctionActor._
  import context._

  def receive: Receive = handleAuction(initAuction)

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