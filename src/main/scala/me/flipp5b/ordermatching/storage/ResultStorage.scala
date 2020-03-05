package me.flipp5b.ordermatching.storage

import java.nio.file.{Files, Paths}

import me.flipp5b.ordermatching.domain.{Accounts, Stock}

class ResultStorage(fileName: String) {
  def write(accounts: Accounts): Unit = {
    val lines = for (account <- accounts.all.sortBy(_.clientName)) yield {
      val stocks = account.stocks
      s"${account.clientName}\t${account.money}\t${stocks(Stock("A"))}\t${stocks(Stock("B"))}\t${stocks(Stock("C"))}\t${stocks(Stock("D"))}"
    }
    import scala.jdk.CollectionConverters._
    Files.write(Paths.get(fileName), lines.asJava)
  }
}
