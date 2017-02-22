package org.north.auction.domain

import akka.actor.{Actor, ActorLogging, Props}

object AuctionActor {
  case class StartAuction(auction: Auction)
  case class BidInAuction(bid: Bid)
  def props = Props[AuctionActor]
}

class AuctionActor extends Actor with ActorLogging {
  import AuctionActor._
  import context._

  def receive = {
    case StartAuction(auction) =>
      sender ! "ok"
      become(handleAuction(auction))
  }

  def handleAuction(auction: Auction): Receive = {
    case BidInAuction(bid) => auction.bid(bid) match {
      case result@Right(newAuction) =>
        sender ! result
        become(handleAuction(newAuction))
      case result@Left(failureReason) =>
        sender ! result
    }
  }
}