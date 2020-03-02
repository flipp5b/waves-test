package me.flipp5b.ordermatching.domain

import org.scalatest.{FlatSpec, Matchers}

class AccountSpec extends FlatSpec with Matchers {
  "Account" should "correctly apply deal with existing stock" in {
    val account = Account("C1", 50, Map((Stock("A"), 5), (Stock("B"), 10)))
    val deal = Deal(Stock("B"), 11, 4)

    val expectedAccount = Account("C1", 94, Map((Stock("A"), 5), (Stock("B"), 6)))

    account.commitDeal(deal, Sell) shouldEqual expectedAccount
  }

  "Account" should "correctly apply deal with new stock" in {
    val account = Account("C2", 45, Map((Stock("A"), 10), (Stock("C"), 6)))
    val deal = Deal(Stock("B"), 11, 4)

    val expectedAccount = Account("C2", 1, Map((Stock("A"), 10), (Stock("B"), 4), (Stock("C"), 6)))

    account.commitDeal(deal, Buy) shouldEqual expectedAccount
  }

  "Account" should "validate order" in {
    val account = Account("C2", 45, Map((Stock("A"), 10)))
    val order = Order("C2", Buy, Stock("A"), 10, 10)

    account.check(order) shouldEqual false
  }
}
