package org.north.auction.domain.model

trait BidFailureReason {
  def message: String
}
case object AmountTooLow extends BidFailureReason { val message = "bid amount too low" }
case object AuctionExpired extends BidFailureReason { val message = "auction expired" }

case class Auction(id: String, seller: User, product: Product, highestBid: Bid, expires: Long) {
  def bid(bidProposal: Bid): Either[BidFailureReason, Auction] =
    if(bidProposal.amount < highestBid.amount) {
      Left(AmountTooLow)
    } else if (bidProposal.timestamp >= expires) {
      Left(AuctionExpired)
    } else {
      Right(copy(highestBid = bidProposal))
    }
}