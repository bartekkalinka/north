package org.north.auction.port.adapter.http

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.pattern.ask
import akka.util.Timeout
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import org.north.auction.port.adapter.http.protocol.StartAuctionRequest

import scala.concurrent.ExecutionContextExecutor

class AuctionController(auctionActor: ActorRef)(implicit val system: ActorSystem, executor: ExecutionContextExecutor, materializer: ActorMaterializer) {
  import JsonProtocol._
  implicit val timeout = Timeout(50, TimeUnit.SECONDS)

  val auctionStartRoute = pathPrefix("auctions")  {
    pathEndOrSingleSlash {
      (post & entity(as[StartAuctionRequest])) { request =>
        complete {
          (auctionActor ? request.prepareStartAuction).mapTo[String].map[ToResponseMarshallable] { a =>
            OK -> a
          }
        }
      }
    }
  }

  val routes = auctionStartRoute
}

