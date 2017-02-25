package org.north.auction.util

import java.time.{LocalDateTime, ZoneOffset}

case class TimeUtils(now: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)) {
  private def epochSec(dt: LocalDateTime): Long = dt.toEpochSecond(ZoneOffset.UTC)
  def current = epochSec(now)
  def daysFromNow(daysNumber: Int): Long = epochSec(now.plusDays(daysNumber))
}

