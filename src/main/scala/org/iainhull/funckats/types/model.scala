package org.iainhull.funckats.types

import org.scalactic.{Every, Good, Bad, Or}
import org.scalactic.Requirements._

class MoneyAmount private (val value: BigDecimal) extends AnyVal with WrappedValue[BigDecimal] {

  def + (rhs: MoneyAmount): MoneyAmount = new MoneyAmount(value + rhs.value)

  def * (rhs: Quantity) = new MoneyAmount(value * rhs.value)
  def / (rhs: Quantity) = new MoneyAmount(value / rhs.value)
}

object MoneyAmount extends WrappedValue.Companion[BigDecimal, MoneyAmount] {
  val Zero: MoneyAmount = new MoneyAmount(0)

  def from(value: BigDecimal): MoneyAmount Or String = {
    if (value >= 0) Good(new MoneyAmount(value))
    else Bad(s"MoneyAmount($value) must be greater than or equal to zero")
  }

  def apply(value: String)(implicit ev: WrappedValue.EnableUnsafe[MoneyAmount]): MoneyAmount = {
    apply(BigDecimal(value))
  }
}

class Quantity private (val value: Int) extends AnyVal with WrappedValue[Int]

object Quantity extends WrappedValue.Companion[Int, Quantity] {
  def from(value: Int): Quantity Or String = {
    if (value > 0) Good(new Quantity(value))
    else Bad(s"Quantity($value) must be greater than zero")
  }
}

/**
  * Created by iain.hull on 20/12/2015.
  */
case class Customer(name: String,
                    preferredCurrency: Currency)

case class Order(customer: Customer,
                 subtotal: MoneyAmount,
                 shipping: MoneyAmount,
                 currency: Currency,
                 items: Every[OrderItem])

case class OrderItem(productId: String,
                     price: MoneyAmount,
                     quantity: Quantity)

case class Sale(order: Order,
                payment: Payment,
                creditCardCharge: MoneyAmount) {
  require(order.currency == payment.currency)
}

case class Payment(currency: Currency, amount: MoneyAmount, reference: String)

trait OrderError

case class InvalidProduct(productId: String) extends OrderError

case class PaymentError(message: String) extends OrderError
