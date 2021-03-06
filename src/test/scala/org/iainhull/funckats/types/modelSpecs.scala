package org.iainhull.funckats.types

class CustomerSpec extends BasicSpec {
  "Customer.apply" should "not accept an invalid currency" in {
    intercept[IllegalArgumentException] {
      Customer("Joe", "SFO")
    }
  }
}

class OrderSpec extends BasicSpec {
  val customer = Customer("Joe", "USD")
  val items = Vector(OrderItem("a", 1, 1))

  "Order.apply" should "not accept an invalid subtotal" in {
    intercept[IllegalArgumentException] {
      Order(customer, -1, 0, "USD", items)
    }
  }

  it should "not accept an invalid shipping" in {
    intercept[IllegalArgumentException] {
      Order(customer, 0, -1, "USD", items)
    }
  }

  it should "not accept an invalid currency" in {
    intercept[IllegalArgumentException] {
      Order(customer, -1, 0, "USD", items)
    }
  }

  it should "not accept an invalid items" in {
    intercept[IllegalArgumentException] {
      Order(customer, 0, 0, "USD", Vector())
    }
  }
}


class OrderItemSpec extends BasicSpec {
  "OrderItem.apply" should "not accept an invalid price" in {
    intercept[IllegalArgumentException] {
      OrderItem("a", -1, 1)
    }
  }

  it should "not accept an invalid quantity" in {
    intercept[IllegalArgumentException] {
      OrderItem("a", 0, 0)
    }
    intercept[IllegalArgumentException] {
      OrderItem("a", 0, -1)
    }
  }
}

class SaleSpec extends BasicSpec {
  val customer = Customer("Joe", "USD")
  val items = Vector(OrderItem("a", BigDecimal(1), 1))
  val order = Order(customer, 10, 0, "USD", items)
  val payment1 = Payment("EUR", 10, "Payment from joe")
  val payment2 = Payment("USD", 10, "Payment from joe")

  "Sale.apply" should "not accept an order and payment with different currencies" in {
    intercept[IllegalArgumentException] {
      Sale(order, payment1, 10)
    }
  }

  it should "not accept an invalid creditCardCharge" in {
    intercept[IllegalArgumentException] {
      Sale(order, payment2, -1)
    }
  }
}
