package me.flipp5b.ordermatching.domain

import org.scalatest.{FlatSpec, Matchers}

class MatcherSpec extends FlatSpec with Matchers {
  private val accounts = Accounts(
    Account("C1", 50, Map((Stock("A"), 5), (Stock("B"), 10))),
    Account("C2", 45, Map((Stock("A"), 10), (Stock("C"), 6))))

  "Matcher" should "perform basic matching" in {
    val orders = List(
      Order("C1", Sell, Stock("B"), 10, 4),
      Order("C1", Sell, Stock("A"), 4, 2),
      Order("C2", Buy, Stock("A"), 3, 100),
      Order("C2", Buy, Stock("B"), 11, 4))

    val expectedState = new MatchingState(
      Map(
        (Stock("A"), new PendingOrders(
          List(Order("C1", Sell, Stock("A"), 4, 2)),
          List(Order("C2", Buy, Stock("A"), 3, 100)))),
        (Stock("B"), new PendingOrders(
          Nil,
          Nil))),
      Accounts(
        Account("C1", 94, Map((Stock("A"), 5), (Stock("B"), 6))),
        Account("C2", 1, Map((Stock("A"), 10), (Stock("B"), 4), (Stock("C"), 6)))))

    val actualState = Matcher.process(accounts, orders)

    actualState.pendingOrdersByStock shouldEqual expectedState.pendingOrdersByStock
    actualState.accounts shouldEqual expectedState.accounts
  }

  "Matcher" should "perform partial matching" in {
    val orders = List(
      Order("C1", Buy, Stock("A"), 5, 3),
      Order("C1", Buy, Stock("A"), 5, 4),
      Order("C2", Sell, Stock("A"), 4, 10))

    val expectedState = new MatchingState(
      Map(
        (Stock("A"), new PendingOrders(
          List(Order("C2", Sell, Stock("A"), 4, 3)),
          Nil))),
      Accounts(
        Account("C1", 15, Map((Stock("A"), 12), (Stock("B"), 10))),
        Account("C2", 80, Map((Stock("A"), 3), (Stock("C"), 6)))))

    val actualState = Matcher.process(accounts, orders)

    actualState.pendingOrdersByStock shouldEqual expectedState.pendingOrdersByStock
    actualState.accounts shouldEqual expectedState.accounts
  }

  "Matcher" should "reject invalid orders" in {
    val orders = List(
      Order("C1", Buy, Stock("A"), 100, 3),
      Order("C2", Sell, Stock("A"), 100, 3))

    val expectedState = new MatchingState(
      Map(
        (Stock("A"), new PendingOrders(
          List(Order("C2", Sell, Stock("A"), 100, 3)),
          Nil))),
      Accounts(
        Account("C1", 50, Map((Stock("A"), 5), (Stock("B"), 10))),
        Account("C2", 45, Map((Stock("A"), 10), (Stock("C"), 6)))))

    val actualState = Matcher.process(accounts, orders)

    actualState.pendingOrdersByStock shouldEqual expectedState.pendingOrdersByStock
    actualState.accounts shouldEqual expectedState.accounts
  }

  "Matcher" should "perform matching in chronological order" in {
    val orders = List(
      Order("C1", Buy, Stock("A"), 4, 3),
      Order("C1", Buy, Stock("A"), 4, 10),
      Order("C2", Sell, Stock("A"), 4, 5))

    val expectedState = new MatchingState(
      Map(
        (Stock("A"), new PendingOrders(
          Nil,
          List(Order("C1", Buy, Stock("A"), 4, 8))))),
      Accounts(
        Account("C1", 30, Map((Stock("A"), 10), (Stock("B"), 10))),
        Account("C2", 65, Map((Stock("A"), 5), (Stock("C"), 6)))))

    val actualState = Matcher.process(accounts, orders)

    actualState.pendingOrdersByStock shouldEqual expectedState.pendingOrdersByStock
    actualState.accounts shouldEqual expectedState.accounts
  }
}
