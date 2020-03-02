package me.flipp5b.ordermatching.domain

import me.flipp5b.ordermatching.domain.Types.Money

case class Account(clientName: String, money: Money, stocks: Map[Stock, Int]) {
  def check(order: Order): Boolean =
    order.orderType match {
      case Buy => money >= order.sum
      case Sell => stocks(order.stock) >= order.quantity
    }

  def commitDeal(deal: Deal, orderType: OrderType): Account =
    orderType match {
      case Buy => copy(
        money = money - deal.sum,
        stocks = stocks.updatedWith(deal.stock) {
          _.map(_ + deal.quantity).orElse(Some(deal.quantity))
        })
      case Sell => copy(
        money = money + deal.sum,
        stocks = stocks.updatedWith(deal.stock) {
          _.map(_ - deal.quantity).orElse(Some(deal.quantity))
        })
    }
}
