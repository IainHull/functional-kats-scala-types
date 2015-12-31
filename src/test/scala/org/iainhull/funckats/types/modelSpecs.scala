package org.iainhull.funckats.types

import MoneyAmount.Unsafe._

class OrderSpec extends BasicSpec {
  val customer = Customer("Joe", Currency.USD)
  val items = Vector(OrderItem("a", MoneyAmount(1), Quantity.from(1).get))

  "Order.apply" should "not accept an invalid items" in {
    intercept[IllegalArgumentException] {
      Order(customer, MoneyAmount(0), MoneyAmount(0), Currency.USD, Vector())
    }
  }
}

class SaleSpec extends BasicSpec {
  val customer = Customer("Joe", Currency.USD)
  val items = Vector(OrderItem("a", MoneyAmount(1), Quantity.from(1).get))
  val order = Order(customer, MoneyAmount(10), MoneyAmount(0), Currency.USD, items)
  val payment1 = Payment(Currency.EUR, MoneyAmount(10), "Payment from joe")
  val payment2 = Payment(Currency.USD, MoneyAmount(10), "Payment from joe")

  "Sale.apply" should "not accept an order and payment with different currencies" in {
    intercept[IllegalArgumentException] {
      Sale(order, payment1, MoneyAmount(10))
    }
  }
}
