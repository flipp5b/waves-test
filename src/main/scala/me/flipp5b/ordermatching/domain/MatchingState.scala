package me.flipp5b.ordermatching.domain

case class MatchingState(pendingOrdersByStock: Map[Stock, PendingOrders], accounts: Accounts) {
  def getPendingOrders(stock: Stock): Option[PendingOrders] = pendingOrdersByStock.get(stock)

  def updated(stock: Stock, pendingOrders: PendingOrders, accounts: Accounts = this.accounts): MatchingState =
    copy(pendingOrdersByStock = pendingOrdersByStock.updated(stock, pendingOrders), accounts = accounts)
}

case object MatchingState {
  def apply(accounts: Accounts): MatchingState = MatchingState(Map.empty, accounts)
}
