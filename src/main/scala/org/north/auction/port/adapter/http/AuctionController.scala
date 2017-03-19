package org.north.auction.port.adapter.http

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.pattern.{AskTimeoutException, ask}
import akka.util.Timeout
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import org.north.auction.domain.AuctionActor
import org.north.auction.domain.AuctionActor.GetAuction
import org.north.auction.domain.model.Auction
import org.north.auction.port.adapter.http.protocol.AuctionRequest.InvalidRequestReason
import org.north.auction.port.adapter.http.protocol.StartAuctionRequest

import scala.concurrent.{ExecutionContextExecutor, Future}

class AuctionController()(implicit val system: ActorSystem, executor: ExecutionContextExecutor, materializer: ActorMaterializer) {
  import JsonProtocol._
  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  val log = Logging(system, getClass)

  val startAuctionRoute =
    pathPrefix("auctions") {
      pathEndOrSingleSlash {
        (post & entity(as[StartAuctionRequest])) { request =>
          complete {
            val validationResult = request.prepareStartAuction
            validationResult match {
              case Left(reason: InvalidRequestReason) =>
                Future.successful(reason.message)
                  .map[ToResponseMarshallable] {
                  BadRequest -> _
                }
              case Right(auction) =>
                val actor = system.actorOf(AuctionActor.props(auction), auction.id)
                log.info(s"created auction actor ${actor.path}")
                Future.successful(auction.id)
                  .map[ToResponseMarshallable] {
                  OK -> _
                }
            }
          }
        }
      }
    }

  val getAuctionRoute =
    pathPrefix("auctions" / Remaining ) { auctionId =>
      pathEndOrSingleSlash {
        get {
          complete {
            val actor = system.actorSelection(s"akka://auctions/user/$auctionId")
            (actor ? GetAuction).mapTo[Auction].map(Right(_)).recover {
              case e: AskTimeoutException =>
                Left(s"auction $auctionId either does not exist or its actor does not respond")
            }.map[ToResponseMarshallable] {
              case Right(a) => OK -> a
              case Left(msg) => NotFound -> msg
            }
          }
        }
      }
    }

  val routes =
    startAuctionRoute ~
    getAuctionRoute
}

