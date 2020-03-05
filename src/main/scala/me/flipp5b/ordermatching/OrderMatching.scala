package me.flipp5b.ordermatching

import me.flipp5b.ordermatching.domain.Matcher
import me.flipp5b.ordermatching.storage.{AccountStorage, OrderStorage, ResultStorage}

object OrderMatching extends App {
  println("Loading accounts from file...")
  val accounts = new AccountStorage("resources/clients.txt").readAll

  println("Loading order log from file...")
  val orders = new OrderStorage("resources/orders.txt").readAll

  println("Matching orders...")
  var finalState = Matcher.process(accounts, orders)

  println(s"Final state: $finalState")

  println("Writing results to file...")
  new ResultStorage("resources/results.txt").write(finalState.accounts)
}
