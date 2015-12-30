package org.iainhull.funckats.types

import org.scalactic.Or
import org.scalactic.Requirements._
import org.scalactic.TypeCheckedTripleEquals._

/**
  * Created by iain.hull on 21/12/2015.
  */
trait OrderService {
  /**
    * The productService is a function that takes a productId and quantity
    * returning a tuple of unit price and base currency.
    */
  def productService: (String, Int) => (BigDecimal, Currency) Or OrderError


  /**
    * The payment service is a function that takes a customer, currency and
    * amount returning a Payment or an OrderError if there.
    */
  def paymentService: (Customer, Currency, BigDecimal) => Payment Or OrderError


  /**
    * Create an initial order for the specified customer, product and quantity
    *
    * @param customer the customer
    * @param productId the first product of the order
    * @param quantity the quantity of the first product
    *
    * @return the initial order or an OrderError if the
    */
  def createOrder(customer: Customer, productId: String, quantity: Int): Order Or OrderError = {
    require(quantity >= 0)


    productService(productId, quantity) map {
      case (basePrice, baseCurrency) =>
        val convert = Currency.convert(baseCurrency, customer.preferredCurrency) _
        val price = convert(basePrice)

        val items = Vector(OrderItem(productId, price, quantity))
        val subTotal = calculateSubtotal(items)

        Order(
          customer,
          subTotal,
          calculateDelivery(quantity, customer.preferredCurrency),
          customer.preferredCurrency,
          items)
    }
  }


  /**
    * Add an item to an existing order
    *
    * @param order the order to amend
    * @param productId the product to add to the order
    * @param quantity the quantity of the added product
    *
    * @return the amended order or an OrderError if the product is not correct
    */
  def addItem(order: Order, productId: String, quantity: Int): Order Or OrderError = {
    require(quantity >= 0)

    productService(productId, quantity) map {
      case (basePrice, baseCurrency) =>

        val convert = Currency.convert(baseCurrency, order.currency) _
        val price = convert(basePrice)

        val newItems = order.items :+ OrderItem(productId, price, quantity)
        val newSubTotal = calculateSubtotal(newItems)

        order.copy(
          subtotal = newSubTotal,
          shipping = order.shipping + calculateDelivery(quantity, order.currency),
          items = newItems)
    }
  }


  /**
    * Change the currency of the specified order, recalculating all amounts
    *
    * @param order the order to convert
    * @param newCurrency the new currency of the order
    *
    * @return the converted order
    */
  def changeCurrency(order: Order, newCurrency: Currency): Order = {

    val convert = Currency.convert(order.currency, newCurrency) _

    val newItems = order.items.map { item =>
      item.copy(price = convert(item.price))
    }

    order.copy(
      subtotal = convert(order.subtotal),
      shipping = convert(order.shipping),
      currency = newCurrency,
      items = newItems)
  }


  /**
    * Checkout the order, process a payment from the customer turning the
    * order into a sale
    *
    * @param order the order to checkout
    *
    * @return the processed Sale or an OrderError if payment wasn't processed
    */
  def checkout(order: Order): Sale Or OrderError = {
    val maybePayment = paymentService(
      order.customer,
      order.currency,
      order.subtotal + order.shipping)

    maybePayment.map { p =>
      Sale(order, p, order.subtotal * creditCardRate(order.customer))
    }
  }


  def calculateSubtotal(items: Vector[OrderItem]): BigDecimal = {
    require(items.nonEmpty)

    items.map { o => o.price * o.quantity }
      .sum
  }

  def calculateDelivery(items: Int, currency: Currency): BigDecimal = {
    Currency.convert(Currency.USD, currency)(items * BigDecimal(2))
  }

  def creditCardRate(customer: Customer): BigDecimal = {
    if (customer.preferredCurrency === Currency.USD)
      BigDecimal("0.015")
    else
      BigDecimal("0.025")
  }
}
