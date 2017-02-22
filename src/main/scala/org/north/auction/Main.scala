package org.north.auction

import java.time.{LocalDateTime, ZoneOffset}
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import org.north.auction.domain.{Auction, AuctionActor, Bid}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends App {
	val system = ActorSystem("auctions")
	val actor1 = system.actorOf(AuctionActor.props)
  def epochSec(dt: LocalDateTime): Long = dt.toEpochSecond(ZoneOffset.UTC)
  def now: Long = epochSec(LocalDateTime.now(ZoneOffset.UTC))
  def daysFromNow(daysNumber: Int): Long = epochSec(LocalDateTime.now(ZoneOffset.UTC).plusDays(daysNumber))
  implicit val timeout = Timeout(50, TimeUnit.SECONDS)

  val startAuctionResult = Await.result(actor1 ? AuctionActor.StartAuction(Auction("chris", "iphone", Bid("chris", 0, now), daysFromNow(14))), Duration.Inf)
  println(s"startAuctionResult $startAuctionResult")
  val bid1Result = Await.result(actor1 ? AuctionActor.BidInAuction(Bid("william", 34, now)), Duration.Inf)
  println(s"bid1Result $bid1Result")
  val bid2Result = Await.result(actor1 ? AuctionActor.BidInAuction(Bid("heather", 32, now)), Duration.Inf)
  println(s"bid2Result $bid2Result")
  val bid3Result = Await.result(actor1 ? AuctionActor.BidInAuction(Bid("heather", 36, daysFromNow(15))), Duration.Inf)
  println(s"bid3Result $bid3Result")
  system.terminate()
}
