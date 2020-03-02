package me.flipp5b.ordermatching.domain

import me.flipp5b.ordermatching.domain.Types.Money

case class Order(clientName: String, orderType: OrderType, stock: Stock, price: Money, quantity: Int) {
  def canMatch(other: Order): Boolean = {
    val (sellPrice, buyPrice) = if (orderType == Sell) (price, other.price) else (other.price, price)
    sellPrice <= buyPrice
  }

  def cannotMatch(other: Order): Boolean = !canMatch(other)

  def sum: Money = quantity * price
}


sealed trait OrderType

case object Sell extends OrderType

case object Buy extends OrderType