package me.flipp5b.ordermatching.domain

import org.scalatest.{FlatSpec, Matchers}

class DealSpec extends FlatSpec with Matchers {
  "Deal" should "be correctly created from matching orders" in {
    val sell = Order("C1", Sell, Stock("B"), 10, 5)
    val buy = Order("C2", Buy, Stock("B"), 11, 4)

    Deal(sell, buy) shouldEqual Deal(Stock("B"), 11, 4)
  }
}
