package org.north.auction.domain

import akka.actor.{Actor, ActorLogging, Props}
import org.north.auction.domain.model.{Auction, Bid}

object AuctionActor {
  trait Command
  case class BidInAuction(bid: Bid) extends Command
  def props(auction: Auction): Props = Props(new AuctionActor(auction))
}

class AuctionActor(initAuction: Auction) extends Actor with ActorLogging {
  import AuctionActor._
  import context._

  def receive: Receive = handleAuction(initAuction)

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