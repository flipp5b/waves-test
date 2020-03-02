package me.flipp5b.ordermatching.domain

import me.flipp5b.ordermatching.domain.Types.Money

case class PendingOrders private (ordersMap: Map[OrderType, List[Order]]) {
  def this(sells: List[Order], buys: List[Order]) = this(Map((Sell, sells), (Buy, buys)))

  def getMatching(orderType: OrderType): List[Order] = {
    val matchingType = if (orderType == Sell) Buy else Sell
    ordersMap(matchingType)
  }

  def updated(orderType: OrderType, orders: List[Order]): PendingOrders =
    copy(ordersMap = ordersMap.updated(orderType, orders))

  def insert(order: Order): PendingOrders = {
    copy(ordersMap = ordersMap.updatedWith(order.orderType) { opt =>
      implicit val comparator: Ordering[Money] =
        if (order.orderType == Sell) Ordering.Int
        else Ordering.Int.reverse
      opt.map(insert(_, order)).orElse(Some(List(order)))
    })
  }

  private def insert(orders: List[Order], order: Order)(implicit comparator: Ordering[Money]): List[Order] =
    orders match {
      case Nil => List(order)
      case h :: tail if comparator.gteq(order.price, h.price) => h :: insert(tail, order)
      case _ => order :: orders
    }
}

case object PendingOrders {
  def apply(): PendingOrders = new PendingOrders(Nil, Nil)
}
