package org.iainhull.funckats.types

import org.scalactic.{Bad, Good}

/**
  * Unit test for the OrderService
  */
class OrderServiceSpec extends BasicSpec {

  object TestOrderService extends OrderService {
    val catalog = Map(
      "a" -> ((BigDecimal(10), "USD")),
      "b" -> ((BigDecimal(20), "USD")),
      "c" -> ((BigDecimal(30), "USD")),
      "d" -> ((BigDecimal(40), "USD")))

    override val productService = { (productId: String, quantity: Int) =>
      catalog.get(productId) match {
        case Some(value) => Good(value)
        case None => Bad(InvalidProduct(productId))
      }
    }

    override val paymentService = { (customer: Customer, currency: String, amount: BigDecimal) =>
      if (currency == "USD") {
        Good(Payment(currency, amount, customer.name))
      } else {
        Bad(PaymentError(s"Cannot process payment for ${customer.name}"))
      }
    }
  }

  import TestOrderService._

  val theCustomer = Customer("Joe Blogs", "EUR")

  "createOrder" should "create an order when the product id is valid" in {
    val maybeOrder = createOrder(theCustomer, "a", 2)
    inside(maybeOrder) {
      case Good(Order(customer, subtotal, shipping, currency, Vector(item))) =>
        customer should be(theCustomer)
        subtotal should be(BigDecimal("18.4020"))
        shipping should be(BigDecimal("3.68040"))
        currency should be("EUR")
        item.productId should be("a")
        item.price should be(BigDecimal("9.2010"))
        item.quantity should be(2)
    }
  }

  it should "fail to create an order when the product id is invalid" in {
    val maybeOrder = createOrder(theCustomer, "bad product", 2)
    maybeOrder should be(Bad(InvalidProduct("bad product")))
  }

  it should "throw IllegalArgumentException if the quantity is less than 0" in {
    intercept[IllegalArgumentException] {
      createOrder(theCustomer, "a", -1)
    }
  }

  "addItem" should "add an item to the order when the product id is valid" in {

    val Good(o1) = createOrder(theCustomer, "a", 2)
    val maybeOrder = addItem(o1, "b", 3)

    inside(maybeOrder) {
      case Good(Order(customer, subtotal, shipping, currency, Vector(i1, i2))) =>
        customer should be(theCustomer)
        subtotal should be(BigDecimal("73.608"))
        shipping should be(BigDecimal("9.201000"))
        currency should be("EUR")
        i1.productId should be("a")
        i1.price should be(BigDecimal("9.20100"))
        i1.quantity should be(2)
        i2.productId should be("b")
        i2.price should be(BigDecimal("18.4020"))
        i2.quantity should be(3)
    }
  }

  it should "fail to create an order when the product id is invalid" in {

    val Good(o1) = createOrder(theCustomer, "a", 2)
    val maybeOrder = addItem(o1, "bad product", 2)

    maybeOrder should be(Bad(InvalidProduct("bad product")))
  }

  it should "throw IllegalArgumentException when the quantity is less than 0" in {

    val Good(o1) = createOrder(theCustomer, "a", 2)
    intercept[IllegalArgumentException] {
      addItem(o1, "a", -1)
    }
  }

  "changeCurrency" should "change the currency of an order applying the fx rate" in {

    val convert = Currency.convert("EUR", "USD") _
    val Good(o1) = createOrder(theCustomer, "a", 2)

    val order = changeCurrency(o1, "USD")

    inside(order) {
      case Order(customer, subtotal, shipping, currency, Vector(i1)) =>
        customer should be(o1.customer)
        subtotal should be(convert(o1.subtotal))
        shipping should be(convert(o1.shipping))
        currency should be("USD")
        i1.price should be(convert(o1.items(0).price))
        i1.productId should be(o1.items(0).productId)
        i1.quantity should be(o1.items(0).quantity)
    }
  }

  it should "throw IllegalArgumentException when the currency is invalid" in {
    val Good(o1) = createOrder(theCustomer, "a", 2)

    intercept[IllegalArgumentException] {
      changeCurrency(o1, "SFO")
    }
  }

  "checkout" should "return a sale when the payment is valid" in {

    val Good(o1) = createOrder(theCustomer, "a", 2)
    val Good(o2) = addItem(o1, "b", 3)
    val theOrder = changeCurrency(o2, "USD")

    val maybeSale = checkout(theOrder)

    inside(maybeSale) {
      case Good(Sale(order, payment, creditCardCharge)) =>
        order should be(theOrder)
        payment.amount should be(order.subtotal + order.shipping)
        payment.amount should be(BigDecimal(90))
        creditCardCharge should be(BigDecimal(2))
    }
  }

  it should "fail when the payment failes" in {

    val Good(o1) = createOrder(theCustomer, "a", 2)
    val Good(theOrder) = addItem(o1, "b", 3)

    val maybeSale = checkout(theOrder)

    maybeSale should be(Bad(PaymentError("Cannot process payment for Joe Blogs")))
  }
}
