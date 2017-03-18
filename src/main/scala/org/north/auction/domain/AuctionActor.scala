package org.north.auction.domain

import akka.actor.{Actor, ActorLogging, Props}
import org.north.auction.domain.model.{Auction, Bid}

object AuctionActor {
  trait Command
  case class StartAuction(auction: Auction) extends Command
  case class BidInAuction(bid: Bid) extends Command
  def props = Props[AuctionActor]
}

class AuctionActor extends Actor with ActorLogging {
  import AuctionActor._
  import context._

  def receive = {
    case StartAuction(auction) =>
      sender ! auction.id
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