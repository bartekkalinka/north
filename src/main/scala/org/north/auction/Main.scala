package org.north.auction

import java.time.{LocalDateTime, ZoneOffset}
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import org.north.auction.domain.model.{Auction, Bid, Product, User}
import org.north.auction.domain.AuctionActor
import org.north.auction.port.adapter.http.AuctionController

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends App {
  implicit val system = ActorSystem("auctions")
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val auctionActor = system.actorOf(AuctionActor.props)
  val controller = new AuctionController(auctionActor)

  Http().bindAndHandle(controller.routes, "0.0.0.0", 9000)

  //TODO transform below into AuctionActor's test:
//	val actor1 = system.actorOf(AuctionActor.props)
//  implicit val timeout = Timeout(50, TimeUnit.SECONDS)
//
//  val (chris, william, heather) = (User("chris"), User("william"), User("heather"))
//  val auction = Auction(chris, Product("iphone"), Bid(chris, 0, now), daysFromNow(14))
//  val startAuctionResult = Await.result(actor1 ? AuctionActor.StartAuction(auction), Duration.Inf)
//  println(s"startAuctionResult $startAuctionResult")
//  val bid1Result = Await.result(actor1 ? AuctionActor.BidInAuction(Bid(william, 34, now)), Duration.Inf)
//  println(s"bid1Result $bid1Result")
//  val bid2Result = Await.result(actor1 ? AuctionActor.BidInAuction(Bid(heather, 32, now)), Duration.Inf)
//  println(s"bid2Result $bid2Result")
//  val bid3Result = Await.result(actor1 ? AuctionActor.BidInAuction(Bid(heather, 36, daysFromNow(15))), Duration.Inf)
//  println(s"bid3Result $bid3Result")
//  system.terminate()
}
