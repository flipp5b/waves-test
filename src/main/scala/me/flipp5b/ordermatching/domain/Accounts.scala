package me.flipp5b.ordermatching.domain

case class Accounts private (storage: Map[String, Account]) {
  def check(order: Order): Boolean = storage(order.clientName).check(order)

  def commitDeal(order: Order, matchingOrder: Order): Either[ValidationError, Accounts] =
    List(order, matchingOrder).filterNot(check) match {
      case Nil =>
        val deal = Deal(order, matchingOrder)
        val newStorage = storage.updatedWith(order.clientName)(_.map(_.commitDeal(deal, order.orderType)))
          .updatedWith(matchingOrder.clientName)(_.map(_.commitDeal(deal, matchingOrder.orderType)))
        Right(copy(storage = newStorage))

      case invalidOrders =>
        Left(ValidationError(invalidOrders.toSet))
    }

  def all: Seq[Account] = storage.values.toSeq
}

case class ValidationError(invalidOrders: Set[Order])

case object Accounts {
  def apply(accounts: Account*): Accounts = {
    val map = accounts.map(a => (a.clientName, a)).toMap
    Accounts(map)
  }
}
