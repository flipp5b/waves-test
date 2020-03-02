package me.flipp5b.ordermatching.domain

import me.flipp5b.ordermatching.domain.Types.Money

case class Deal(stock: Stock, price: Money, quantity: Int) {
  def sum: Money = quantity * price
}

case object Deal {
  def apply(order: Order, matchingOrder: Order): Deal = {
    require(order canMatch matchingOrder)

    val price = Math.max(order.price, matchingOrder.price)
    val quantity = Math.min(order.quantity, matchingOrder.quantity)
    Deal(order.stock, price, quantity)
  }
}