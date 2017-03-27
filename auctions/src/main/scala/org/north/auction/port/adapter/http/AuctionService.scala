package org.north.auction.port.adapter.http

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.event.Logging
import akka.pattern.{AskTimeoutException, ask}
import akka.util.Timeout
import org.north.auction.domain.AuctionActor
import org.north.auction.domain.AuctionActor.GetAuction
import org.north.auction.domain.model.{Auction, BidFailureReason}
import org.north.auction.port.adapter.http.protocol.AuctionRequest.InvalidRequestReason
import org.north.auction.port.adapter.http.protocol.{BidRequest, StartAuctionRequest}

import scala.concurrent.Future

object AuctionService {
  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  def startAuction(request: StartAuctionRequest)(implicit system: ActorSystem): Future[Either[String, String]] = {
    val log = Logging(system, getClass)
    val validationResult = request.prepareStartAuction
    validationResult match {
      case Left(reason: InvalidRequestReason) =>
        Future.successful(Left(reason.message))
      case Right(startAuction) =>
        val actor = system.actorOf(AuctionActor.props(startAuction), startAuction.id)
        log.info(s"created auction actor ${actor.path}")
        Future.successful(Right(startAuction.id))
    }
  }

  def getAuction(auctionId: String)(implicit system: ActorSystem): Future[Either[String, Auction]] = {
    import system.dispatcher
    val actor = system.actorSelection(s"akka://auctions/user/$auctionId")
    (actor ? GetAuction).mapTo[Auction].map(Right(_)).recover {
      case e: AskTimeoutException =>
        Left(s"auction $auctionId either does not exist or its actor does not respond")
    }
  }

  def bidInAuction(auctionId: String, request: BidRequest)(implicit system: ActorSystem): Future[Either[String, Auction]] = {
    import system.dispatcher
    val actor = system.actorSelection(s"akka://auctions/user/$auctionId")
    (actor ? request.toBidInAuction)
      .mapTo[Either[BidFailureReason, Auction]]
      .map(_.swap.map(_.message).swap)
      .recover {
        case e: AskTimeoutException =>
          Left(s"auction $auctionId either does not exist or its actor does not respond")
      }
  }
}

