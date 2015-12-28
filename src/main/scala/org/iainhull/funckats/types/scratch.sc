import org.iainhull.funckats.types._

val c1 = Customer("Johnny Rotten", "USD")

// Customer("Joe Blogs", "SFO")

val o1 = Order(c1, BigDecimal(200), BigDecimal(10), "USD", Vector(OrderItem("prod 1", BigDecimal(3), 10)))

// Order(c1, BigDecimal(-200), BigDecimal(10), "USD", Vector(OrderItem("prod 1", BigDecimal(3), 10)))
// Order(c1, BigDecimal(200), BigDecimal(-10), "USD", Vector(OrderItem("prod 1", BigDecimal(3), 10)))
// Order(c1, BigDecimal(200), BigDecimal(10), "SFO", Vector(OrderItem("prod 1", BigDecimal(3), 10)))
// Order(c1, BigDecimal(200), BigDecimal(10), "USD", Vector())

