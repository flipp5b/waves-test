package me.flipp5b.ordermatching.domain

object Matcher {
  def process(accounts: Accounts, orders: List[Order]): MatchingState =
    orders.foldLeft(MatchingState(accounts))(applyOrder)

  private def applyOrder(state: MatchingState, order: Order): MatchingState = {
    val pendingOrders = state.getPendingOrders(order.stock).getOrElse(PendingOrders())
    val (newPendingOrders, newAccounts) = applyOrder(pendingOrders, order, state.accounts)
    state.updated(order.stock, newPendingOrders, newAccounts)
  }

  private def applyOrder(pendingOrders: PendingOrders, order: Order, accounts: Accounts): (PendingOrders, Accounts) = {
    def doMatch(matchingOrder: Order, matchingOrdersTail: List[Order]): (PendingOrders, Accounts) =
      accounts.commitDeal(order, matchingOrder) match {
        case Right(newAccounts) if order.quantity == matchingOrder.quantity =>
          val newPendingOrders = pendingOrders.updated(matchingOrder.orderType, matchingOrdersTail)
          (newPendingOrders, newAccounts)

        case Right(newAccounts) if order.quantity < matchingOrder.quantity =>
          val newMatchingOrder = matchingOrder.copy(quantity = matchingOrder.quantity - order.quantity)
          val newPendingOrders = pendingOrders.updated(matchingOrder.orderType, newMatchingOrder :: matchingOrdersTail)
          (newPendingOrders, newAccounts)

        case Right(newAccounts) =>
          val newPendingOrders = pendingOrders.updated(matchingOrder.orderType, matchingOrdersTail)
          val newOrder = order.copy(quantity = order.quantity - matchingOrder.quantity)
          applyOrder(newPendingOrders, newOrder, newAccounts)

        case Left(ValidationError(invalidOrders)) if invalidOrders(matchingOrder) =>
          val newPendingOrders = pendingOrders.updated(matchingOrder.orderType, matchingOrdersTail)
          applyOrder(newPendingOrders, order, accounts)

        case Left(ValidationError(_)) =>
          (pendingOrders, accounts)
      }

    pendingOrders getMatching order.orderType match {
      case Nil =>
        (pendingOrders insert order, accounts)

      case matchingOrder :: _ if order cannotMatch matchingOrder =>
        (pendingOrders insert order, accounts)

      case matchingOrder :: matchingOrdersTail =>
        doMatch(matchingOrder, matchingOrdersTail)
    }
  }
}