package org.north.auction.port.adapter.http

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import org.north.auction.port.adapter.http.protocol.{BidRequest, StartAuctionRequest}

class AuctionController()(implicit val system: ActorSystem, materializer: ActorMaterializer) {
  import system.dispatcher
  import JsonProtocol._

  val log = Logging(system, getClass)

  val startAuctionRoute =
    pathPrefix("auctions") {
      pathEndOrSingleSlash {
        (post & entity(as[StartAuctionRequest])) { request =>
          complete {
            AuctionService.startAuction(request).map[ToResponseMarshallable] {
              case Right(a) => OK -> a
              case Left(msg) => BadRequest -> msg
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
            AuctionService.getAuction(auctionId).map[ToResponseMarshallable] {
              case Right(a) => OK -> a
              case Left(msg) => NotFound -> msg
            }
          }
        }
      }
    }

  val bidRoute =
    pathPrefix("auctions" / Remaining) { auctionId =>
      pathEndOrSingleSlash {
        (put & entity(as[BidRequest])) { request =>
          complete {
            AuctionService.bidInAuction(auctionId, request).map[ToResponseMarshallable] {
                case Right(a) => OK -> a
                case Left(msg) => BadRequest -> msg
              }
          }
        }
      }
    }

  val routes =
    startAuctionRoute ~
    getAuctionRoute ~
    bidRoute
}

