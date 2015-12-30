package org.iainhull.funckats.types

import org.scalactic.Requirements._

/**
  * Created by iain.hull on 20/12/2015.
  */
case class Customer(name: String,
                    preferredCurrency: Currency)

case class Order(customer: Customer,
                 subtotal: BigDecimal,
                 shipping: BigDecimal,
                 currency: Currency,
                 items: Vector[OrderItem]) {
  require(subtotal >= 0)
  require(shipping >= 0)
  require(items.nonEmpty)
}

case class OrderItem(productId: String,
                     price: BigDecimal,
                     quantity: Int) {
  require(price >= 0)
  require(quantity > 0)
}

case class Sale(order: Order,
                payment: Payment,
                creditCardCharge: BigDecimal) {
  require(order.currency == payment.currency)
  require(creditCardCharge >= 0)
}

case class Payment(currency: Currency, amount: BigDecimal, reference: String)

trait OrderError

case class InvalidProduct(productId: String) extends OrderError

case class PaymentError(message: String) extends OrderError
