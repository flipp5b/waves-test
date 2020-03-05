package me.flipp5b.ordermatching.storage

object Loan {
  def using[A <: AutoCloseable, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }
}
