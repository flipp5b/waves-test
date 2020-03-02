package me.flipp5b.ordermatching.domain

import org.scalatest.{FlatSpec, Matchers}

class PendingOrdersSpec extends FlatSpec with Matchers {
  "PendingOrders" should "keeps correct ordering by price and time after insertions" in {
    val pendingOrders = new PendingOrders(
      sells = List(
        Order("C1", Sell, Stock("A"), 5, 42),
        Order("C2", Sell, Stock("A"), 6, 42),
        Order("C3", Sell, Stock("A"), 7, 42)),
      buys = List(
        Order("C1", Buy, Stock("A"), 7, 42),
        Order("C2", Buy, Stock("A"), 6, 42),
        Order("C3", Buy, Stock("A"), 5, 42)))

    val updatedPendingOrders = pendingOrders.insert(Order("C4", Sell, Stock("A"), 4, 42))
      .insert(Order("C5", Sell, Stock("A"), 6, 42))
      .insert(Order("C4", Buy, Stock("A"), 4, 42))
      .insert(Order("C5", Buy, Stock("A"), 7, 42))

    updatedPendingOrders shouldEqual new PendingOrders(
      sells = List(
        Order("C4", Sell, Stock("A"), 4, 42),
        Order("C1", Sell, Stock("A"), 5, 42),
        Order("C2", Sell, Stock("A"), 6, 42),
        Order("C5", Sell, Stock("A"), 6, 42),
        Order("C3", Sell, Stock("A"), 7, 42)),
      buys = List(
        Order("C1", Buy, Stock("A"), 7, 42),
        Order("C5", Buy, Stock("A"), 7, 42),
        Order("C2", Buy, Stock("A"), 6, 42),
        Order("C3", Buy, Stock("A"), 5, 42),
        Order("C4", Buy, Stock("A"), 4, 42)))
  }
}
