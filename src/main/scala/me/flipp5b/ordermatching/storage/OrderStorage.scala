package me.flipp5b.ordermatching.storage

import me.flipp5b.ordermatching.domain.{Buy, Order, Sell, Stock}
import me.flipp5b.ordermatching.storage.Loan.using

import scala.io.Source

class OrderStorage(fileName: String) {
  def readAll: List[Order] =
    using(Source.fromFile(fileName)) { source =>
      (for (line <- source.getLines) yield parse(line)).toList
    }

  private def parse(line: String): Order = {
    line.split("\\t") match {
      case Array(clientName, orderTypeStr, stockCode, price, quantity) =>
        val orderType = orderTypeStr match {
          case "s" => Sell
          case "b" => Buy
        }
        Order(clientName, orderType, Stock(stockCode), price.toInt, quantity.toInt)
    }
  }
}
