package me.flipp5b.ordermatching.domain

import org.scalatest.{FlatSpec, Matchers}

class AccountsSpec extends FlatSpec with Matchers {
  "Accounts" should "correctly commit deal" in {
    val accounts = Accounts(
      Account("C1", 50, Map((Stock("A"), 5), (Stock("B"), 10))),
      Account("C2", 45, Map((Stock("A"), 10), (Stock("C"), 6))))

    val sell = Order("C1", Sell, Stock("B"), 10, 5)
    val byu = Order("C2", Buy, Stock("B"), 11, 4)

    val expectedAccounts = Accounts(
      Account("C1", 94, Map((Stock("A"), 5), (Stock("B"), 6))),
      Account("C2", 1, Map((Stock("A"), 10), (Stock("B"), 4), (Stock("C"), 6))))

    accounts.commitDeal(sell, byu) shouldEqual Right(expectedAccounts)
  }
}
