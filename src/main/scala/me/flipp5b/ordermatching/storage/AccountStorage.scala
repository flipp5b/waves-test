package me.flipp5b.ordermatching.storage

import me.flipp5b.ordermatching.storage.Loan.using
import me.flipp5b.ordermatching.domain.{Account, Accounts, Stock}

import scala.io.Source

class AccountStorage(fileName: String) {
  def readAll: Accounts =
    using(Source.fromFile(fileName)) { source =>
      val accounts = for (line <- source.getLines) yield parse(line)
      Accounts(accounts.toSeq: _*)
    }

  private def parse(line: String): Account = {
    line.split("\\t") match {
      case Array(clientName, money, a, b, c, d) =>
        val stocks = Map(
          (Stock("A"), a.toInt),
          (Stock("B"), b.toInt),
          (Stock("C"), c.toInt),
          (Stock("D"), d.toInt))
        Account(clientName, money.toInt, stocks)
    }
  }
}
