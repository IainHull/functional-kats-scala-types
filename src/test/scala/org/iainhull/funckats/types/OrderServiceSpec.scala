package org.iainhull.funckats.types

import org.scalactic.{Bad, Good }

/**
  * Unit test for the OrderService
  */
class OrderServiceSpec extends BasicSpec {
  import MoneyAmount.Unsafe._

  object TestOrderService extends OrderService {
    val catalog = Map(
      "a" -> ((MoneyAmount(10), Currency.USD)),
      "b" -> ((MoneyAmount(20), Currency.USD)),
      "c" -> ((MoneyAmount(30), Currency.USD)),
      "d" -> ((MoneyAmount(40), Currency.USD)))

    override val productService = { (productId: String, quantity: Quantity) =>
      catalog.get(productId) match {
        case Some(value) => Good(value)
        case None => Bad(InvalidProduct(productId))
      }
    }

    override val paymentService = { (customer: Customer, currency: Currency, amount: MoneyAmount) =>
      if (currency == Currency.USD) {
        Good(Payment(currency, amount, customer.name))
      } else {
        Bad(PaymentError(s"Cannot process payment for ${customer.name}"))
      }
    }
  }

  import TestOrderService._

  val theCustomer = Customer("Joe Blogs", Currency.EUR)

  "createOrder" should "create an order when the product id is valid" in {
    val maybeOrder = createOrder(theCustomer, "a", Quantity.from(2).get)
    inside(maybeOrder) {
      case Good(Order(customer, subtotal, shipping, currency, Vector(item))) =>
        customer should be(theCustomer)
        subtotal should be(MoneyAmount("18.4020"))
        shipping should be(MoneyAmount("3.68040"))
        currency should be(Currency.EUR)
        item.productId should be("a")
        item.price should be(MoneyAmount("9.2010"))
        item.quantity should be(Quantity.from(2).get)
    }
  }

  it should "fail to create an order when the product id is invalid" in {
    val maybeOrder = createOrder(theCustomer, "bad product", Quantity.from(2).get)
    maybeOrder should be(Bad(InvalidProduct("bad product")))
  }

  "addItem" should "add an item to the order when the product id is valid" in {

    val Good(o1) = createOrder(theCustomer, "a", Quantity.from(2).get)
    val maybeOrder = addItem(o1, "b", Quantity.from(3).get)

    inside(maybeOrder) {
      case Good(Order(customer, subtotal, shipping, currency, Vector(i1, i2))) =>
        customer should be(theCustomer)
        subtotal should be(MoneyAmount("73.608"))
        shipping should be(MoneyAmount("9.201000"))
        currency should be(Currency.EUR)
        i1.productId should be("a")
        i1.price should be(MoneyAmount("9.20100"))
        i1.quantity should be(Quantity.from(2).get)
        i2.productId should be("b")
        i2.price should be(MoneyAmount("18.4020"))
        i2.quantity should be(Quantity.from(3).get)
    }
  }

  it should "fail to create an order when the product id is invalid" in {

    val Good(o1) = createOrder(theCustomer, "a", Quantity.from(2).get)
    val maybeOrder = addItem(o1, "bad product", Quantity.from(2).get)

    maybeOrder should be(Bad(InvalidProduct("bad product")))
  }

  "changeCurrency" should "change the currency of an order applying the fx rate" in {

    val convert = Currency.convert(Currency.EUR, Currency.USD) _
    val Good(o1) = createOrder(theCustomer, "a", Quantity.from(2).get)

    val order = changeCurrency(o1, Currency.USD)

    inside(order) {
      case Order(customer, subtotal, shipping, currency, Vector(i1)) =>
        customer should be(o1.customer)
        subtotal should be(convert(o1.subtotal))
        shipping should be(convert(o1.shipping))
        currency should be(Currency.USD)
        i1.price should be(convert(o1.items(0).price))
        i1.productId should be(o1.items(0).productId)
        i1.quantity should be(o1.items(0).quantity)
    }
  }

  "checkout" should "return a sale when the payment is valid" in {

    val Good(o1) = createOrder(theCustomer, "a", Quantity.from(2).get)
    val Good(o2) = addItem(o1, "b", Quantity.from(3).get)
    val theOrder = changeCurrency(o2, Currency.USD)

    val maybeSale = checkout(theOrder)

    inside(maybeSale) {
      case Good(Sale(order, payment, creditCardCharge)) =>
        order should be(theOrder)
        payment.amount should be(order.subtotal + order.shipping)
        payment.amount should be(MoneyAmount(90))
        creditCardCharge should be(MoneyAmount(2))
    }
  }

  it should "fail when the payment fails" in {

    val Good(o1) = createOrder(theCustomer, "a", Quantity.from(2).get)
    val Good(theOrder) = addItem(o1, "b", Quantity.from(3).get)

    val maybeSale = checkout(theOrder)

    maybeSale should be(Bad(PaymentError("Cannot process payment for Joe Blogs")))
  }
}
